package rpg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import rpg.game.Location;
import rpg.game.WorldMap;
import rpg.game.WorldMapReader;

public class WorldMapTest {
	@Test
	public void connectAddsLocations() {
		WorldMap top = WorldMap.createEmptyMap();
		top.connect(new Location("a", "b"), new Location("a", "c"));
		assertTrue(top.locations().contains(new Location("a", "b")));
		assertTrue(top.locations().contains(new Location("a", "c")));
	}
	
	@Test
	public void singleLocationHasNoAdjacentLocations() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1");
		assertTrue(top.locationsAdjacentTo(new Location("region 1", "place 1")).isEmpty());
	}
}
