package rpg.test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rpg.game.Game;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;
import rpg.tcp.ServerOutputPort;
import rpg.tcp.TcpGameServer;
import rpg.test.support.ClientStub;
import rpg.test.support.Tests;

public class TcpGamePortTest {
	private final WorldMap map = Tests.testMap().createMap();
	private final CharacterLocations charLocations = new CharacterLocations(map);
	
	private final ClientStub clientOne = new ClientStub();
	private final ClientStub clientTwo = new ClientStub();
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final CyclicBarrier synchPoint = new CyclicBarrier(2);
	private final TcpGameServer server = new TcpGameServer(new ServerOutputPort() {
		@Override
		public void listening(int port) {
			try {
				synchPoint.await();
			} catch (InterruptedException | BrokenBarrierException e) {
			}
		}
		@Override
		public void clientConnected(InetAddress inetAddress) {
		}
		@Override
		public void cannotListen(String cause) {
			synchPoint.reset();
		}
	}, new Game("Testland", charLocations));

	@Before
	public void connectToGame() throws IOException, InterruptedException, ExecutionException, BrokenBarrierException {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				server.listen(6799);
			}
		});
		try {
			synchPoint.await();
			charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
			charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		} catch (BrokenBarrierException e) {
			throw new AssertionError("Connection failed", e);
		}
	}

	@Test
	public void enterAsJim() throws IOException {
		clientOne.connectToServer();
		clientOne.send("enter as jim");
		clientOne.received("Welcome to Testland, jim!");
	}
	
	@Test
	public void mustEnterGameToKnowLocation() throws IOException {
		clientOne.connectToServer();
		clientOne.send("where");
		clientOne.received("Enter the game first.");
	}

	@Test
	public void afterEnteringGameCanKnowLocation() throws IOException {
		clientOne.connectToServer();
		clientOne.send("enter as jim");
		clientOne.send("where");
		clientOne.received("Welcome to Testland, jim!");
		clientOne.received("You're in an open field, County of the Mage.");
	}
	
	@Test
	public void unknownCommandsAreReported() throws IOException {
		clientOne.connectToServer();
		clientOne.send("foo and bar");
		clientOne.received("What does that mean?");
	}
	
	@Test
	public void jimSpeaksToJohn() throws IOException {
		clientOne.connectToServer();
		clientTwo.connectToServer();
		
		clientOne.send("enter as jim");
		clientTwo.send("enter as john");
		
		clientOne.receive();
		clientTwo.receive();

		clientOne.send("say hi");
		clientTwo.received("jim: hi");
	}
	
	@Test
	public void johnSpeaksBack() throws IOException {
		clientOne.connectToServer();
		clientTwo.connectToServer();
		
		clientOne.send("enter as jim");
		clientTwo.send("enter as john");
		
		clientOne.receive();
		clientTwo.receive();

		clientOne.send("say hi");
		clientTwo.received("jim: hi");
		
		clientTwo.send("say hello!");
		clientOne.received("john: hello!");
	}
	
	@Test
	public void quit() throws IOException {
		clientOne.connectToServer();
		clientOne.send("enter as jim");
		clientOne.receive();
		clientOne.send("quit");
		clientOne.received("Bye.");
	}
	
	@After
	public void shutdownClientsAndServer() throws IOException, InterruptedException, ExecutionException {
		clientOne.close();
		clientTwo.close();
		server.shutdown();
		executor.shutdown();
		executor.awaitTermination(500, TimeUnit.MILLISECONDS);
	}
}
