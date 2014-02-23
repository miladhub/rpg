package rpg.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rpg.game.CommandContext;

public class TcpGameServer {
	private final ExecutorService pool = Executors.newFixedThreadPool(10);
	private final ServerOutputPort out;
	private CommandContext game;
	private ServerSocket welcomeSocket;
	private volatile boolean shutdown;
	
	public TcpGameServer(ServerOutputPort out, CommandContext game) {
		super();
		this.out = out;
		this.game = game;
	}

	public void listen(int port) {
		try {
			welcomeSocket = new ServerSocket(port);
			out.listening(port);
			while (!shutdown) {
				Socket socket = welcomeSocket.accept();
				pool.submit(new ClientHandler(game, socket));
				out.clientConnected(socket.getInetAddress());
			}
		} catch (IOException e) {
			out.cannotListen(e.getMessage());
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
