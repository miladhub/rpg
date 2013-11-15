package rpg.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientStub {
	private Socket clientSocket;
	public void connectToServer() throws UnknownHostException, IOException {
		clientSocket = new Socket("localhost", 6789);
	}
	public void close() throws IOException {
		if (clientSocket != null && !clientSocket.isClosed())
			clientSocket.close();
	}
	public void send(String message) throws IOException {
		new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true).println(message);
	}
	public String receive() throws IOException {
		return new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
	}
	public void received(String message) throws IOException {
		assertEquals(message, receive());
	}
}
