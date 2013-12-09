package rpg.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import rpg.game.LocalMap;
import rpg.game.Location;
import rpg.game.WorldMap;

public class WorldMapBuilderTest {
	@Test
	public void buildEmpty() {
		WorldMap top = new WorldMap.WorldMapBuilder().createMap();
		assertTrue(top.regions().isEmpty());
		assertTrue(top.locations().isEmpty());
	}
	
	@Test
	public void buildOneRegion() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region");
		b.addPlace("place 1");
		b.addPlace("place 2");
		
		WorldMap top = b.createMap();
		assertEquals(new HashSet<>(Arrays.asList("region")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region", "place 1"),
				new Location("region", "place 2"))), top.locations());
	}
	
	@Test
	public void buildOneRegionWithSize() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region");
		b.addPlace("place 1").size("3x4");
		
		WorldMap top = b.createMap();
		assertEquals(new HashSet<>(Arrays.asList("region")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region", "place 1"))), top.locations());
		assertEquals(new LocalMap(3, 4), top.localMap(new Location("region", "place 1")));
	}
	
	@Test
	public void defaultSizeIs5x5() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region");
		b.addPlace("place 1");
		
		WorldMap top = b.createMap();
		assertEquals(new HashSet<>(Arrays.asList("region")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region", "place 1"))), top.locations());
		assertEquals(new LocalMap(5, 5), top.localMap(new Location("region", "place 1")));
	}
	
	@Test
	public void buildTwoRegions() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region 1");
		b.addPlace("place 1");
		b.addPlace("place 2");
		b.addRegion("region 2");
		b.addPlace("place 3");
		b.addPlace("place 4");
		
		WorldMap top = b.createMap();
		assertEquals(new HashSet<>(Arrays.asList("region 1", "region 2")), top.regions());
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"),
				new Location("region 1", "place 2"), new Location("region 2", "place 3"), new Location("region 2", "place 4"))),
				top.locations());
	}
	
	@Test
	public void subsequentPlacesAreAdjacent() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region 1");
		b.addPlace("place 1");
		b.addPlace("place 2");
		
		WorldMap top = b.createMap();
		
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 2"))),
				top.locationsAdjacentTo(new Location("region 1", "place 1")));
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"))),
				top.locationsAdjacentTo(new Location("region 1", "place 2")));
	}
	
	@Test
	public void locationsSeparatedByAtLeastOnePlaceAreNotAdjacent() {
		WorldMap.WorldMapBuilder b = new WorldMap.WorldMapBuilder();
		b.addRegion("region 1");
		b.addPlace("place 1");
		b.addPlace("place 2");
		b.addRegion("region 2");
		b.addPlace("place 3");
		b.addPlace("place 4");
		
		WorldMap top = b.createMap();
		
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 2"))),
				top.locationsAdjacentTo(new Location("region 1", "place 1")));
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 1"), new Location("region 2", "place 3"))),
				top.locationsAdjacentTo(new Location("region 1", "place 2")));
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 1", "place 2"), new Location("region 2", "place 4"))),
				top.locationsAdjacentTo(new Location("region 2", "place 3")));
		assertEquals(new HashSet<>(Arrays.asList(new Location("region 2", "place 3"))),
				top.locationsAdjacentTo(new Location("region 2", "place 4")));
	}
}
