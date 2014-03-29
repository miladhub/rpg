package rpg.game;

import java.util.Set;

public interface CommandContext extends GameContext {
	CharacterLocations characterLocations();
	Set<String> nearbyCharacters(String character);
	Set<String> characters();
}
