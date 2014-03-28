package rpg.game;

public class EnterGame implements Command {
	private final String player;
	private final OutputPort out;

	public EnterGame(String player, OutputPort out) {
		this.player = player;
		this.out = out;
	}

	@Override
	public void execute(CommandContext commandContext) {
		commandContext.addCharacter(player, out);
		commandContext.outputPort(player).heardFromGame("Welcome to " + commandContext.worldName() + ", " 
				+ player + "!");
	}

	@Override
	public String character() {
		return player;
	}
}
