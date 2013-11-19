package rpg.game;

public class QuitGame implements Command {
	private final String character;

	public QuitGame(String character) {
		this.character = character;
	}

	@Override
	public void execute(CommandContext commandContext) {
		commandContext.quit(character);
	}
}
