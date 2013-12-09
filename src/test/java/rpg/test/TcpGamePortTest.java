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

import rpg.game.WorldMap;
import rpg.game.CharacterLocations;
import rpg.tcp.ServerOutputPort;
import rpg.tcp.TcpGameServer;

public class TcpGamePortTest {
	private final WorldMap map = new WorldMap.WorldMapBuilder()
		.addRegion("County of the Mage")
		.addPlace("an open field").size("5x5")
		.addPlace("a field next to the previous one")
		.addPlace("the Mage border")
		.addRegion("the County of the Warrior")
		.addPlace("the Warrior border")
		.createMap();
	private final CharacterLocations charLocations = new CharacterLocations(map);
	private final ClientStub clientOne = new ClientStub();
	private final ClientStub clientTwo = new ClientStub();
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final CyclicBarrier synchPoint = new CyclicBarrier(2);
	private final TcpGameServer server = new TcpGameServer("Testland", new ServerOutputPort() {
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
	}, charLocations);

	@Before
	public void connectToGame() throws IOException, InterruptedException, ExecutionException, BrokenBarrierException {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				server.listen(6789);
			}
		});
		synchPoint.await();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
	}

	@Test
	public void enterAsJim() throws IOException {
		clientOne.connectToServer();
		clientOne.send("enter as jim");
		clientOne.received("Welcome to Testland, jim!");
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
