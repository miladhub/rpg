package rpg.game;

public class EnterGame implements Command {
	private final OutputPort out;

	public EnterGame(OutputPort out) {
		this.out = out;
	}

	@Override
	public void execute(String character, CommandContext commandContext) {
		commandContext.addCharacter(character, out);
		commandContext.outputPort(character).heardFromGame("Welcome to " + commandContext.worldName() + ", " 
				+ character + "!");
	}
}
