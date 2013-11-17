package rpg.game;


public class Travel implements Command {
	private final String travelling;
	private final String where;

	public Travel(String travelling, String where) {
		this.travelling = travelling;
		this.where = where;
	}

	@Override
	public void execute(CommandContext context) {
		try {
			context.characterLocations().moveToPlace(travelling, where);
		} catch (CannotGoThereException e) {
			context.outputPort(travelling).heardFromGame("You can't travel to " + where + " from your current location.");
		}
		notifyNearbyCharacters(context);
	}

	private void notifyNearbyCharacters(CommandContext context) {
		for (String otherCharacter : context.charactersOthersThan(travelling)) {
			Location movingCharLocation = context.characterLocations().whereIs(travelling);
			Location otherCharLocation = context.characterLocations().whereIs(otherCharacter);
			if (movingCharLocation.place().equals(otherCharLocation.place())) {
				context.outputPort(otherCharacter).sees(travelling);
				context.outputPort(travelling).sees(otherCharacter);
			}
		}
	}
}
