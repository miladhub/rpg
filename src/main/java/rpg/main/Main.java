package rpg.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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

	public Main(String gameName, String mapFile, String charactersFile) throws IOException {
		map = WorldMapReader.readFromString(FileUtils.readFileToString(new File(mapFile)));
		game = new Game(gameName, readCharactersFromFile(map, new File(charactersFile)));
		server = new TcpGameServer(new ClientConsole(), game);
		clock = new Clock(game);
	}
	
	private CharacterLocations readCharactersFromFile(WorldMap map, File charactersFile) throws IOException {
		CharacterReader charLocReader = new CharacterReader(map);
		return charLocReader.readFromString(FileUtils.readFileToString(charactersFile));
	}
	
	public static void main(String[] args) throws Exception {
		final Main main = new Main("The game", "map.txt", "characters.txt");
		addShutdownHook(main);
		main.start();
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
}
