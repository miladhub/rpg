package rpg.game;

import java.util.Set;

public interface CommandContext {
	String worldName();
	void addCharacter(String character, OutputPort out);
	void removeCharacter(String character);
	OutputPort outputPort(String character);
	CharacterLocations characterLocations();
	Set<String> nearbyCharacters(String character);
}
