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

	public InputPort enterAs(String character, OutputPort out) {
		outs.put(character, out);
		out.heardFromGame("Welcome to " + worldName + ", " + character + "!");
		return new Session(this, character);
	}

	public void said(String speaker, String what) {
		new Speak(speaker, what).execute(this);
	}
	
	public void moved(String movingChar, String where) {
		new Move(movingChar, where).execute(this);
	}

	@Override
	public OutputPort outputPort(String otherCharacter) {
		return outs.get(otherCharacter);
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

	public void tellWhereaboutOf(String character) {
		Location location = charLocations.whereIs(character);
		outputPort(character).heardFromGame("You're in " + location.place() + ", " + location.region() + ".");
	}

	public void tellWhatsNear(String character) {
		outputPort(character).heardFromGame("You can go to:");
		for (Location near : charLocations.locationsAdjacentTo(character)) {
			outputPort(character).heardFromGame("\t" + near.place());
		}
	}

	@Override
	public void regionChangedTo(String character, String region) {
		outputPort(character).heardFromGame("You have crossed into " + region + ".");
	}
}
