package rpg.game;

import java.util.Set;

public interface GameContext {
	String worldName();
	void addCharacter(String character, OutputPort out);
	void removeCharacter(String character);
	Set<String> characters();
	OutputPort outputPort(String character);
}
