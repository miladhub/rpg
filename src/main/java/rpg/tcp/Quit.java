package rpg.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Quit implements Command {
	private final Socket clientSocket;

	public Quit(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void execute(PrintWriter writer) throws IOException {
		writer.println("Bye.");
		clientSocket.close();
	}
}
