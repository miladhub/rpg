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
		for (String otherCharacter : context.nearbyCharacters(travelling)) {
			context.outputPort(otherCharacter).sees(travelling,
					context.characterLocations().localPosition(travelling),
					context.characterLocations().localMap(travelling));
			context.outputPort(travelling).sees(otherCharacter,
					context.characterLocations().localPosition(otherCharacter),
					context.characterLocations().localMap(otherCharacter));
		}
	}
}
