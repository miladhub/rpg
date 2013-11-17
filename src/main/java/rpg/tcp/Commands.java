package rpg.tcp;

import rpg.game.Command;
import rpg.game.Say;
import rpg.game.TellWhatsNear;
import rpg.game.TellWhereabout;
import rpg.game.Travel;

public class Commands {
	private final ClientContext context;

	public Commands(ClientContext context) {
		this.context = context;
	}

	public Command createCommand(String command) throws UnknownCommandException {
		if (command.startsWith("enter as ")) {
			return new StartGame(context, command);
		}
		if ("quit".equals(command)) {
			return new Quit(context);
		}
		if (command.startsWith("say ")) {
			return new Say(context.inputPort().character(), command.substring("say ".length()));
		}
		if (command.startsWith("go to ")) {
			return new Travel(context.inputPort().character(), command.substring("go to ".length()));
		}
		if ("where am I".equalsIgnoreCase(command)) {
			return new TellWhereabout(context.inputPort().character());
		}
		if ("what's near".equalsIgnoreCase(command)) {
			return new TellWhatsNear(context.inputPort().character());
		}
		throw new UnknownCommandException(command);
	}
}
