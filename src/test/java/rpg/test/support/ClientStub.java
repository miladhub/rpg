package rpg.test.support;

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
	private PrintWriter out;
	private BufferedReader in;
	public void connectToServer() throws IOException {
		clientSocket = new Socket("localhost", 6799);
		clientSocket.setSoTimeout(5000);
		out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	public void close() throws IOException {
		if (clientSocket != null && !clientSocket.isClosed())
			clientSocket.close();
	}
	public void send(String message) throws IOException {
		out.println(message);
	}
	public String receive() throws IOException {
		return in.readLine();
	}
	public void received(String message) throws IOException {
		assertEquals(message, receive());
	}
}
