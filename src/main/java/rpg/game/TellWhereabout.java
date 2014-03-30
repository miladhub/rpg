package rpg.game;

public class TellWhereabout implements Command {
	@Override
	public void execute(String character, CommandContext commandContext) {
		Location location = commandContext.characterLocations().whereIs(character);
		commandContext.outputPort(character).heardFromGame(
				"You're in " + location.place() + ", " + location.region() + ".");		
	}
}
