package rpg.game;

public class TellWhereabout implements Command {
	private final String character;

	public TellWhereabout(String character) {
		this.character = character;
	}

	@Override
	public void execute(CommandContext commandContext) {
		Location location = commandContext.characterLocations().whereIs(character);
		commandContext.outputPort(character).heardFromGame(
				"You're in " + location.place() + ", " + location.region() + ".");		
	}

	@Override
	public String character() {
		return character;
	}
}
