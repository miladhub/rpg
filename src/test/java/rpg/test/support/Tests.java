package rpg.test.support;

import rpg.game.WorldMap;
import rpg.game.WorldMap.WorldMapBuilder;

public class Tests {
	public static WorldMapBuilder testMap() {
		return new WorldMap.WorldMapBuilder()
				.addRegion("County of the Mage")
				.addPlace("an open field").size("5x5")
				.addPlace("a field next to the previous one")
				.addPlace("the Mage border")
				.addRegion("the County of the Warrior")
				.addPlace("the Warrior border");
	}
}
