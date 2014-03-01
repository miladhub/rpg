package rpg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import rpg.game.CharacterLocations;
import rpg.game.CharacterReader;
import rpg.game.WorldMap;

public class CharacterReaderTest {
	private final WorldMap map = new WorldMap.WorldMapBuilder()
		.addRegion("County of the Mage")
		.addPlace("an open field").size("5x5")
		.addPlace("a field next to the previous one")
		.addPlace("the Mage border")
		.addRegion("the County of the Warrior")
		.addPlace("the Warrior border")
		.createMap();

	@Test
	public void readCharactersLocations() throws Exception {
		CharacterLocations charLocations = new CharacterLocations(map);
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		charLocations.setCharacterAtLocation("john", "the County of the Warrior", "the Warrior border");
		charLocations.setLocalPosition("john", 2, 2);

		assertEquals(charLocations, new CharacterReader(map).readFromString(
				"jim" + "\t" + "County of the Mage" + "\t" + "an open field" + "\t" + "0, 0\n" +
				"john" + "\t" + "the County of the Warrior" + "\t" + "the Warrior border" + "\t" + "2, 2"));
	}
}
