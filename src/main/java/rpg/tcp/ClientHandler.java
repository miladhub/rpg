package rpg.tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import rpg.game.Game;
import rpg.game.InputPort;
import rpg.game.OutputPort;

public class ClientHandler implements Callable<String>, ClientContext {
	private final Socket clientSocket;
	private final Game game;
	private final BufferedReader reader;
	private final PrintWriter writer;
	private final OutputPort out;
	private InputPort session;

	public ClientHandler(Game game, Socket clientSocket) throws IOException {
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
			public void sees(String whoOrWhat) {
				writer.println("You see: " + whoOrWhat);
			}
		};
	}

	@Override
	public String call() throws Exception {
		try {
			final Commands commands = new Commands(this); 
			String command;
			while ((command = reader.readLine()) != null) {
				try {
					commands.createCommand(command).execute(game);
				} catch (UnknownCommandException e) {
					writer.println("What does that mean?");
				}
			}
			return "done";
		} finally {
			clientSocket.close();
		}
	}
	
	@Override
	public InputPort inputPort() {
		return session;
	}

	@Override
	public void startSession(InputPort session) {
		this.session = session; 
	}

	@Override
	public OutputPort outputPort() {
		return out;
	}
	
	@Override
	public void endSession() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
