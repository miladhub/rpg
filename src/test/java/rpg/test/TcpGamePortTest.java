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
	private static final CharacterLocations LOCATIONS = new CharacterLocations(WorldMap.createEmptyMap());
	private ClientStub clientOne = new ClientStub();
	private ClientStub clientTwo = new ClientStub();
	private ExecutorService executor = Executors.newSingleThreadExecutor();
	private CyclicBarrier synchPoint = new CyclicBarrier(2);
	private TcpGameServer server = new TcpGameServer("Testland", new ServerOutputPort() {
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
	}, LOCATIONS);

	@Before
	public void connectToGame() throws IOException, InterruptedException, ExecutionException, BrokenBarrierException {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				server.listen(6789);
			}
		});
		synchPoint.await();
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
