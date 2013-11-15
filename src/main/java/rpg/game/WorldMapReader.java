package rpg.game;

import rpg.game.WorldMap.WorldMapBuilder;

public class WorldMapReader {
	public static WorldMap readFromString(String map) {
		WorldMapBuilder builder = new WorldMapBuilder();
		String[] tokens = map.split("\n");
		for (String token : tokens) {
			if (token.startsWith("\t")) {
				builder.addPlace(token.substring(1));
			} else {
				builder.addRegion(token);
			}
		}
		return builder.createMap();
	}
}
