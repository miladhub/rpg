package rpg.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game implements ScriptContext, MovementsListener {
	private final String worldName;
	private final Map<String, OutputPort> outs = new HashMap<>();
	private final CharacterLocations charLocations;
	private Set<Script> runningScripts = new HashSet<>();
	private Map<String, Script> blockingScripts = new HashMap<>();

	public Game(String worldName, CharacterLocations charLocations) {
		this.worldName = worldName;
		this.charLocations = charLocations;
		charLocations.registerToMovements(this);
	}

	@Override
	public String worldName() {
		return worldName;
	}

	@Override
	public void addCharacter(String character, OutputPort out) {
		outs.put(character, out);
	}
	
	@Override
	public void removeCharacter(String character) {
		outs.remove(character);
	}
	
	@Override
	public OutputPort outputPort(String character) {
		if (!outs.containsKey(character)) {
			throw new IllegalStateException("No output port defined for character " + character);
		}
		return outs.get(character);
	}
	
	@Override
	public Set<String> nearbyCharacters(String character) {
		Set<String> others = new HashSet<>();
		for (String other : characters()) {
			if (!character.equals(other)) {
				Location movingCharLocation = charLocations.whereIs(character);
				Location otherCharLocation = charLocations.whereIs(other);
				if (movingCharLocation.place().equals(otherCharLocation.place())) {
					others.add(other);
				}
			}
		}
		return others;
	}
	
	@Override
	public Set<String> characters() {
		return outs.keySet();
	}
	
	@Override
	public CharacterLocations characterLocations() {
		return charLocations;
	}

	@Override
	public void regionChangedTo(String character, String region) {
		outputPort(character).heardFromGame("You have crossed into " + region + ".");
	}

	@Override
	public void positionChangedTo(String character, LocalPosition localPosition) {
		outputPort(character).isAt(localPosition, charLocations.localMap(character));
	}

	public void addScript(Script script) {
		runningScripts.add(script);
	}

	public void tick() {
		for (Script script : new HashSet<>(runningScripts)) {
			script.onTick(this);
		}
	}

	@Override
	public void keepBusy(String character, Script script) {
		blockingScripts.put(character, script);		
	}

	public void startScripts() {
		for (Script script : runningScripts) {
			script.onStart(this);
		}
	}

	public void stopScripts() {
		for (Script script : runningScripts) {
			script.onStop(this);
		}		
	}

	@Override
	public boolean characterIsBusy(String character) {
		return blockingScripts.containsKey(character);
	}

	@Override
	public void interrupt(String character) {
		if (characterIsBusy(character)) {
			Script interruptedScript = blockingScripts.remove(character);
			interruptedScript.onStop(this);
			runningScripts.remove(interruptedScript);
		}
	}
}
