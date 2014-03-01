package rpg.main;

import java.io.File;
import java.io.IOException;

import rpg.game.CharacterReader;
import rpg.game.Game;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;
import rpg.game.WorldMapReader;
import rpg.tcp.TcpGameServer;

public class Main {
	private WorldMap map;
	private Game game;
	private TcpGameServer server;
	private Clock clock;

	public static void main(String[] args) throws Exception {
		final Main main = new Main();
		main.setup();
		addShutdownHook(main);
		main.start();
	}

	private void setup() throws IOException {
		map = WorldMapReader.readMapFromFile(new File("map.txt"));
		game = createGame();
		server = createServer();
		clock = createClock();
	}

	private static void addShutdownHook(final Main main) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					main.shutDown();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void start() {
		game.startScripts();
		clock.start();
		server.listen(6789);
	}

	private void shutDown() throws IOException {
		game.stopScripts();
		clock.stop();
		server.shutdown();
	}
	
	private Clock createClock() {
		return new Clock(game);
	}

	private TcpGameServer createServer() {
		return new TcpGameServer(new ClientConsole(), game);
	}

	private Game createGame() throws IOException {
		CharacterLocations locations = readCharacters(map);
		return new Game("The game", locations);
	}

	private CharacterLocations readCharacters(WorldMap map) throws IOException {
		CharacterReader charLocReader = new CharacterReader(map);
		return charLocReader.readFromFile(new File("characters.txt"));
	}
}
