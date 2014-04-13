package rpg.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rpg.game.CommandExecutor;

public class TcpGameServer {
	private final ExecutorService pool = Executors.newFixedThreadPool(10);
	private final ServerOutputPort handle;
	private CommandExecutor game;
	private ServerSocket welcomeSocket;
	private volatile boolean shutdown;
	
	public TcpGameServer(ServerOutputPort handle, CommandExecutor game) {
		super();
		this.handle = handle;
		this.game = game;
	}

	public void listen(int port) {
		try {
			welcomeSocket = new ServerSocket(port);
			handle.listening(port);
			while (!shutdown) {
				Socket socket = welcomeSocket.accept();
				pool.submit(new ClientHandler(game, socket));
				handle.clientConnected(socket.getInetAddress());
			}
		} catch (IOException e) {
			handle.cannotListen(e.getMessage());
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
