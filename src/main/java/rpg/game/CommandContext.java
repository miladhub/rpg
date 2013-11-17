package rpg.game;

import java.util.Set;

public interface CommandContext {
	InputPort enterAs(String character, OutputPort out);
	void quit(String character);
	OutputPort outputPort(String character);
	CharacterLocations characterLocations();
	Set<String> characters();
	Set<String> charactersOthersThan(String character);
}