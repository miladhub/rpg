package rpg.game;

import java.util.Set;

public interface CommandContext {
	OutputPort outputPort(String otherCharacter);
	CharacterLocations characterLocations();
	Set<String> characters();
	Set<String> charactersOthersThan(String character);
}