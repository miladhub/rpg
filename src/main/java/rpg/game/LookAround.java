package rpg.game;

public class LookAround implements Command {

	private String looking;
	public LookAround(String looking) {
		this.looking = looking;
	}

	@Override
	public void execute(CommandContext commandContext) {
		notifyNearbyCharacters(commandContext);
	}
	
	private void notifyNearbyCharacters(CommandContext context) {
		for (String otherCharacter : context.nearbyCharacters(looking)) {
			context.outputPort(looking).sees(otherCharacter,
					context.characterLocations().localPosition(otherCharacter),
					context.characterLocations().localMap(otherCharacter));
		}
	}

}
