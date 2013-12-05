package rpg.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import rpg.game.LocalMap;
import rpg.game.Location;
import rpg.game.WorldMap;
import rpg.game.WorldMapReader;

public class WorldMapReaderTest {
	@Test
	public void readFromStringEmpty() {
		WorldMap top = WorldMapReader.readFromString("");
		assertTrue(top.regions().isEmpty());
		assertTrue(top.locations().isEmpty());
	}
	
	@Test
	public void readFromStringSingleLocation() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1");
		assertEquals(new HashSet<>(Arrays.asList("region 1")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"))), top.locations());
	}
	
	@Test
	public void readFromStringTwoLocations() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1" +
				"\n\tplace 2");
		assertEquals(new HashSet<>(Arrays.asList("region 1")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"), new Location("region 1", "place 2"))), top.locations());
	}
	
	@Test
	public void readFromStringTwoLocationsInDifferentRegions() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1" +
				"\n\tplace 2" +
				"\nregion 2" +
				"\n\tplace 3" +
				"\n\tplace 4");
		assertEquals(new HashSet<>(Arrays.asList("region 1", "region 2")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"), new Location("region 1", "place 2"),
				new Location("region 2", "place 3"), new Location("region 2", "place 4"))), top.locations());
	}
	
	@Test
	public void readLocationWithSize() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1 - 5x4");
		assertEquals(new HashSet<>(Arrays.asList("region 1")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"))), top.locations());
		assertEquals(new LocalMap(5, 4), top.localMap(new Location("region 1", "place 1")));
	}
	
	
	@Test
	public void readMoreLocationsWithSize() {
		WorldMap top = WorldMapReader.readFromString("region 1" +
				"\n\tplace 1 - 5x4" +
				"\n\tplace 2 - 6x7" +
				"\nregion 2" +
				"\n\tplace 3 - 1x2" +
				"\n\tplace 4 - 4x3");
		assertEquals(new HashSet<>(Arrays.asList("region 1", "region 2")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"),
				new Location("region 1", "place 2"), new Location("region 2", "place 3"),
				new Location("region 2", "place 4"))), top.locations());
		assertEquals(new LocalMap(5, 4), top.localMap(new Location("region 1", "place 1")));
		assertEquals(new LocalMap(6, 7), top.localMap(new Location("region 1", "place 2")));
		assertEquals(new LocalMap(1, 2), top.localMap(new Location("region 2", "place 3")));
		assertEquals(new LocalMap(4, 3), top.localMap(new Location("region 2", "place 4")));
	}
}
