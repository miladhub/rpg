package rpg.game;

public class TellWhatsNear implements Command {
	private final String character;

	public TellWhatsNear(String character) {
		this.character = character;
	}

	@Override
	public void execute(CommandContext commandContext) {
		commandContext.outputPort(character).heardFromGame("You can go to:");
		for (Location near : commandContext.characterLocations().locationsAdjacentTo(character)) {
			commandContext.outputPort(character).heardFromGame("\t" + near.place());
		}
	}
}
