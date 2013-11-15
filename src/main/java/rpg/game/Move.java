package rpg.game;

public class Move {
	private final String movingChar;
	private final String where;

	public Move(String movingChar, String where) {
		this.movingChar = movingChar;
		this.where = where;
	}

	public void execute(CommandContext context) {
		try {
			context.characterLocations().moveToPlace(movingChar, where);
		} catch (CannotGoThereException e) {
			context.outputPort(movingChar).heardFromGame("You can't go to " + where + " from your current location.");
		}
		notifyNearbyCharacters(context);		
	}

	private void notifyNearbyCharacters(CommandContext context) {
		for (String otherCharacter : context.charactersOthersThan(movingChar)) {
			Location movingCharLocation = context.characterLocations().whereIs(movingChar);
			Location otherCharLocation = context.characterLocations().whereIs(otherCharacter);
			if (movingCharLocation.place().equals(otherCharLocation.place())) {
				context.outputPort(otherCharacter).sees(movingChar);
				context.outputPort(movingChar).sees(otherCharacter);
			}
		}
	}
}
