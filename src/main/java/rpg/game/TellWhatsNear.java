package rpg.game;

public class TellWhatsNear implements Command {
	@Override
	public void execute(String character, CommandContext commandContext) {
		commandContext.outputPort(character).heardFromGame("You can go to:");
		for (Location near : commandContext.characterLocations().locationsAdjacentTo(character)) {
			commandContext.outputPort(character).heardFromGame("\t" + near.place());
		}
	}
}
