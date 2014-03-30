package rpg.game;

public class LookAround implements Command {
	@Override
	public void execute(String character, CommandContext commandContext) {
		notifyNearbyCharacters(character, commandContext);
	}
	
	private void notifyNearbyCharacters(String character, CommandContext context) {
		for (String otherCharacter : context.nearbyCharacters(character)) {
			context.outputPort(character).sees(otherCharacter,
					context.characterLocations().localPosition(otherCharacter),
					context.characterLocations().localMap(otherCharacter));
		}
	}
}
