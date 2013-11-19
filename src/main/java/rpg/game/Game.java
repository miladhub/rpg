package rpg.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game implements CommandContext, MovementsListener {
	private final String worldName;
	private final Map<String, OutputPort> outs = new HashMap<>();
	private final CharacterLocations charLocations;

	public Game(String worldName, CharacterLocations charLocations) {
		this.worldName = worldName;
		this.charLocations = charLocations;
		charLocations.registerToMovements(this);
	}

	public String worldName() {
		return worldName;
	}

	@Override
	public void enterAs(String character, OutputPort out) {
		outs.put(character, out);
		out.heardFromGame("Welcome to " + worldName + ", " + character + "!");
	}
	
	@Override
	public void quit(String character) {
		outputPort(character).heardFromGame("Bye.");
		outputPort(character).disconnect();
		outs.remove(character);
	}
	
	@Override
	public OutputPort outputPort(String character) {
		return outs.get(character);
	}

	@Override
	public Set<String> characters() {
		return outs.keySet();
	}
	
	@Override
	public Set<String> charactersOthersThan(String character) {
		Set<String> others = new HashSet<>();
		for (String other : characters()) {
			if (!character.equals(other)) {
				others.add(other);
			}
		}
		return others;
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
		outputPort(character).heardFromGame("Local position is now " + localPosition + ".");
	}
}
