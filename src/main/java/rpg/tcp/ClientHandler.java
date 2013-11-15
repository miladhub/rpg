package rpg.tcp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

import rpg.game.Game;
import rpg.game.InputPort;

public class ClientHandler implements Callable<String>, ClientContext {
	private final Socket clientSocket;
	private final Game game;
	private InputPort session;

	public ClientHandler(Game game, Socket clientSocket) {
		super();
		this.game = game;
		this.clientSocket = clientSocket;
	}

	@Override
	public String call() throws Exception {
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			final PrintWriter writer = new PrintWriter(new BufferedOutputStream(clientSocket.getOutputStream()), true);
			final Commands commands = new Commands(game, clientSocket, this); 
			String command;
			while ((command = reader.readLine()) != null) {
				try {
					commands.createCommand(command).execute(writer);
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
	public InputPort session() {
		return session;
	}

	@Override
	public void startSession(InputPort session) {
		this.session = session; 
	}
}
