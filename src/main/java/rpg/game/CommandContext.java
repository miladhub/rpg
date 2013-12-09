package rpg.game;

import java.util.Set;

public interface CommandContext {
	void enterAs(String character, OutputPort out);
	void quit(String character);
	OutputPort outputPort(String character);
	CharacterLocations characterLocations();
	Set<String> nearbyCharacters(String character);
}
