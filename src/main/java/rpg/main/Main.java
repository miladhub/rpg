package rpg.main;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import rpg.game.WorldMap;
import rpg.game.WorldMapReader;
import rpg.game.CharacterLocations;
import rpg.tcp.TcpGameServer;

public class Main {
	public static void main(String[] args) throws Exception {
		WorldMap map = WorldMapReader.readFromString(FileUtils.readFileToString(new File("map.txt")));
		CharacterLocations locations = new CharacterLocations(map);
		locations.setCharacterAtLocation("Jim", "County of the Wizard", "Witch jungle");
		locations.setCharacterAtLocation("John", "Realm of the Warrior", "Barracks");
		final TcpGameServer gameServer = new TcpGameServer("The game", new ConsolePort(), locations);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					gameServer.shutdown();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		gameServer.listen(6789);
	}
}
