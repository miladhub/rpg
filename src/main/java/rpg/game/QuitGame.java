package rpg.game;

public class QuitGame implements Command {
	private final String character;

	public QuitGame(String character) {
		this.character = character;
	}

	@Override
	public void execute(CommandContext commandContext) {
		OutputPort port = commandContext.outputPort(character);
		port.heardFromGame("Bye.");
		port.endSession();
		commandContext.removeCharacter(character);
	}

	@Override
	public String character() {
		return character;
	}
}
