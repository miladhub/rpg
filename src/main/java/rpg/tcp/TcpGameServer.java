package rpg.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rpg.game.Game;
import rpg.game.CharacterLocations;

public class TcpGameServer {
	private final ExecutorService pool = Executors.newFixedThreadPool(10);
	private final ServerOutputPort out;
	private Game game;
	private ServerSocket welcomeSocket;
	private volatile boolean shutdown;
	
	public TcpGameServer(String worldName, ServerOutputPort out, CharacterLocations characterLocations) {
		super();
		this.out = out;
		game = new Game(worldName, characterLocations);
	}

	public void listen(int port) throws Exception {
		welcomeSocket = new ServerSocket(port);
		out.listening(port);
		while (!shutdown) {
			Socket socket = welcomeSocket.accept();
			pool.submit(new ClientHandler(game, socket));
			out.clientConnected(socket.getInetAddress());
		}
	}

	public void shutdown() throws IOException {
		shutdown = true;
		pool.shutdown();
		if (welcomeSocket != null) {
			welcomeSocket.close();
		}
	}
}
