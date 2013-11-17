package rpg.tcp;

import rpg.game.Command;
import rpg.game.CommandContext;

public class WhereAmI implements Command {
	private final ClientContext context;

	public WhereAmI(ClientContext context) {
		this.context = context;
	}

	@Override
	public void execute(CommandContext commandContext) {
		context.inputPort().whereabout();
	}
}
