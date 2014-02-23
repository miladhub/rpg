package rpg.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import rpg.game.Game;
import rpg.game.WorldMap;
import rpg.game.WorldMapReader;
import rpg.game.CharacterLocations;
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
		map = readMapFromFile();
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
		clock.start();
		server.listen(6789);
	}

	private void shutDown() throws IOException {
		clock.stop();
		server.shutdown();
	}
	
	private Clock createClock() {
		return new Clock(game);
	}

	private TcpGameServer createServer() {
		return new TcpGameServer(new ClientConsole(), game);
	}

	private Game createGame() {
		CharacterLocations locations = createFakeCharacters(map);
		return new Game("The game", locations);
	}

	private CharacterLocations createFakeCharacters(WorldMap map) {
		CharacterLocations locations = new CharacterLocations(map);
		locations.setCharacterAtLocation("Jim", "County of the Wizard", "Wizard border");
		locations.setCharacterAtLocation("John", "Realm of the Warrior", "Warrior border");
		locations.setLocalPosition("Jim", 0, 0);
		locations.setLocalPosition("John", 0, 0);
		return locations;
	}

	private WorldMap readMapFromFile() throws IOException {
		return WorldMapReader.readFromString(FileUtils.readFileToString(new File("map.txt")));
	}
}
