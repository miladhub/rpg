package rpg.tcp;

import rpg.game.Command;
import rpg.game.CommandContext;

public class Quit implements Command {
	private final ClientContext context;

	public Quit(ClientContext context) {
		this.context = context;
	}

	@Override
	public void execute(CommandContext commandContext) {
		commandContext.quit(context.inputPort().character());
		context.endSession();
	}
}
