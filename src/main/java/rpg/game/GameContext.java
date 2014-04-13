package rpg.game;

import java.util.Set;

public interface GameContext {
	String worldName();
	void addCharacter(String character, CharacterHandle handle);
	void removeCharacter(String character);
	Set<String> characters();
	CharacterHandle character(String character);
}
