package rpg.tcp;

import rpg.game.Command;
import rpg.game.CommandContext;
import rpg.game.InputPort;

public class StartGame implements Command {
	private final ClientContext context;
	private final String command;

	public StartGame(ClientContext context, String command) {
		this.context = context;
		this.command = command;
	}

	@Override
	public void execute(CommandContext commandContext) {
		String player = command.substring("enter as ".length());
		InputPort session = commandContext.enterAs(player, context.outputPort());
		context.startSession(session);
	}
}
