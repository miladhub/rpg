package rpg.game;

public class EnterGame implements Command {
	private final CharacterHandle handle;

	public EnterGame(CharacterHandle handle) {
		this.handle = handle;
	}

	@Override
	public void execute(String character, CommandContext commandContext) {
		commandContext.addCharacter(character, handle);
		commandContext.character(character).heardFromGame("Welcome to " + commandContext.worldName() + ", " 
				+ character + "!");
	}
}
