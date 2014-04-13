package rpg.game;

public class Travel implements Command {
	private final String where;

	public Travel(String where) {
		this.where = where;
	}

	@Override
	public void execute(String character, CommandContext context) {
		try {
			context.characterLocations().moveToPlace(character, where);
		} catch (CannotGoThereException e) {
			context.character(character).heardFromGame("You can't travel to " + where + " from your current location.");
		}
		notifyNearbyCharacters(character, context);
	}

	private void notifyNearbyCharacters(String character, CommandContext context) {
		for (String otherCharacter : context.nearbyCharacters(character)) {
			context.character(otherCharacter).sees(character,
					context.characterLocations().localPosition(character),
					context.characterLocations().localMap(character));
			context.character(character).sees(otherCharacter,
					context.characterLocations().localPosition(otherCharacter),
					context.characterLocations().localMap(otherCharacter));
		}
	}
}
