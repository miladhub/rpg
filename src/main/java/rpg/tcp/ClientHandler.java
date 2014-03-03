package rpg.tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import rpg.game.CommandContext;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.NotInGameException;
import rpg.game.OutputPort;

public class ClientHandler implements Callable<String> {
	private final Socket clientSocket;
	private final CommandContext game;
	private final BufferedReader reader;
	private final PrintWriter writer;
	private final OutputPort out;

	public ClientHandler(CommandContext game, final Socket clientSocket) throws IOException {
		super();
		this.game = game;
		this.clientSocket = clientSocket;
		reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		writer = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()), true);
		out = new OutputPort() {
			@Override
			public void heardFromGame(String message) {
				writer.println(message);
			}					
			@Override
			public void heardFrom(String from, String what) {
				writer.println(from + ": " + what);
			}
			@Override
			public void sees(String whoOrWhat, LocalPosition where, LocalMap localMap) {
				writer.println(new GridPrinter(where, localMap).print(whoOrWhat));
			}
			@Override
			public void isAt(LocalPosition localPosition, LocalMap localMap) {
				writer.println(new GridPrinter(localPosition, localMap).print("you"));
			}
			@Override
			public void endSession() {
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@Override
	public String call() throws Exception {
		try {
			final Commands commands = new Commands(out);
			String command;
			while ((command = reader.readLine()) != null) {
				try {
					commands.createCommand(command).execute(game);
				} catch (UnknownCommandException e) {
					writer.println("What does that mean?");
				} catch (NotInGameException e) {
					writer.println("Enter the game first.");
				}
			}
			return "done";
		} finally {
			clientSocket.close();
		}
	}
}
