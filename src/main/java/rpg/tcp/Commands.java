package rpg.tcp;

import java.net.Socket;

import rpg.game.Game;

public class Commands {
	private final Game game;
	private final Socket clientSocket;
	private final ClientContext context;

	public Commands(Game game, Socket clientSocket, ClientContext context) {
		this.game = game;
		this.clientSocket = clientSocket;
		this.context = context;
	}

	public Command createCommand(String command) throws UnknownCommandException {
		if ("quit".equals(command)) {
			return new Quit(clientSocket);
		}
		if (command.startsWith("enter as ")) {
			return new StartGame(context, command, game);
		}
		if (command.startsWith("say ")) {
			return new Say(context, command);
		}
		if (command.startsWith("go to ")) {
			return new GoTo(context, command);
		}
		if ("where am I".equalsIgnoreCase(command)) {
			return new WhereAmI(context);
		}
		if ("what's near".equalsIgnoreCase(command)) {
			return new WhatsNear(context);
		}
		throw new UnknownCommandException(command);
	}
}
