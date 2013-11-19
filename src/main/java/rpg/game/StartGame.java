package rpg.game;

public class StartGame implements Command {
	private final String player;
	private final OutputPort out;

	public StartGame(String player, OutputPort out) {
		this.player = player;
		this.out = out;
	}

	@Override
	public void execute(CommandContext commandContext) {
		commandContext.enterAs(player, out);
	}
}
