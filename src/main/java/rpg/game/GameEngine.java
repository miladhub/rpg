package rpg.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameEngine implements ScriptContext, ScriptRunner {
	private final GameContext game;
	private final CharacterLocations charLocations;
	private final List<Script> runningScripts = new ArrayList<>();
	private final Map<String, Script> blockingScripts = new HashMap<>();
	private final Map<Script, Integer> durations = new HashMap<>();
	private final Map<Script, Integer> executionCounters = new HashMap<>();
	private final Map<Script, ScriptSpecification> scriptSpecs = new HashMap<>();

	public GameEngine(GameContext game, CharacterLocations charLocations) {
		this.game = game;
		this.charLocations = charLocations;
	}

	@Override
	public String worldName() {
		return game.worldName();
	}

	@Override
	public void addCharacter(String character, OutputPort out) {
		game.addCharacter(character, out);
	}

	@Override
	public void removeCharacter(String character) {
		game.removeCharacter(character);
	}

	@Override
	public OutputPort outputPort(String character) {
		return game.outputPort(character);
	}

	@Override
	public CharacterLocations characterLocations() {
		return charLocations;
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
		return game.characters();
	}

	@Override
	public void keepBusy(String character, Script script) {
		blockingScripts.put(character, script);		
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
			durations.remove(interruptedScript);
			runningScripts.remove(interruptedScript);
			scriptSpecs.remove(interruptedScript);
		}
	}
	
	@Override
	public void addScript(ScriptSpecificationBuilder specBuilder) {
		ScriptSpecification spec = specBuilder.build();
		runningScripts.add(spec.script());
		scriptSpecs.put(spec.script(), spec);
		if (spec.hasDuration()) {
			durations.put(spec.script(), spec.durationInSeconds());
		}
		executionCounters.put(spec.script(), 0);
		spec.script().onStart(this);
	}

	@Override
	public void tick() {
		for (Script script : new ArrayList<>(runningScripts)) {
			if (mustTick(script)) {
				script.onTick(this);
			}
			checkIfScriptHasFinished(script);
		}
	}

	@Override
	public void stopScripts() {
		for (Script script : runningScripts) {
			script.onStop(this);
		}		
	}
	
	private boolean mustTick(Script script) {
		if (executionCounters.get(script) == 0) {
			executionCounters.put(script, scriptSpecs.get(script).frequencyInSeconds());
			return true;
		} else {
			executionCounters.put(script, executionCounters.get(script) - 1);
			return false;
		}
	}

	private void checkIfScriptHasFinished(Script script) {
		if (durations.containsKey(script)) {
			durations.put(script, durations.get(script) - 1);
			if (durations.get(script) == 0) {
				durations.remove(script);
				runningScripts.remove(script);
				scriptSpecs.remove(script);
				for (String character : blockingScripts.keySet()) {
					if (blockingScripts.get(character) == script) {
						blockingScripts.remove(character);
					}
				}
				script.onStop(this);
			}
		}
	}
}
