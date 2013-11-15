package rpg.tcp;

import java.io.PrintWriter;

import rpg.game.Game;
import rpg.game.InputPort;
import rpg.game.OutputPort;

public class StartGame implements Command {
	private final ClientContext context;
	private final String command;
	private final Game game;

	public StartGame(ClientContext context, String command, Game game) {
		this.context = context;
		this.command = command;
		this.game = game;
	}

	@Override
	public void execute(final PrintWriter writer) {
		String player = command.substring("enter as ".length());
		InputPort session = game.enterAs(player, new OutputPort() {
			@Override
			public void heardFromGame(String message) {
				writer.println(message);
			}					
			@Override
			public void heardFrom(String from, String what) {
				writer.println(from + ": " + what);
			}
			@Override
			public void sees(String whoOrWhat) {
				writer.println("You see: " + whoOrWhat);
			}
		});
		context.startSession(session);
	}

}
