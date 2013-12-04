package rpg.main;

import java.net.InetAddress;

import rpg.tcp.ServerOutputPort;

public class ClientConsole implements ServerOutputPort {
	@Override
	public void listening(int port) {
		System.out.println("Listing on port " + port + " ...");
	}

	@Override
	public void clientConnected(InetAddress address) {
		System.out.println("Client connected from " + address);		
	}

	@Override
	public void cannotListen(String cause) {
		System.err.println("Could not start server: " + cause);
	}
}
