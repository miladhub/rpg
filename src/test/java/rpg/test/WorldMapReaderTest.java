package rpg.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

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
}
