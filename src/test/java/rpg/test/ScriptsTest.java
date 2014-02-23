package rpg.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import rpg.game.CharacterLocations;
import rpg.game.CommandContext;
import rpg.game.Game;
import rpg.game.OutputPort;
import rpg.game.Script;
import rpg.game.TellWhereabout;
import rpg.game.WorldMap;

public class ScriptsTest {
	private final class LocationsTeller implements Script {
		@Override
		public void onTick(CommandContext ctx) {
			for (String character : ctx.characters()) {
				new TellWhereabout(character).execute(ctx);
			}
		}
	}

	private final WorldMap map = new WorldMap.WorldMapBuilder()
		.addRegion("County of the Mage")
		.addPlace("an open field").size("5x5")
		.addPlace("a field next to the previous one")
		.addPlace("the Mage border")
		.addRegion("the County of the Warrior")
		.addPlace("the Warrior border")
		.createMap();

	private final CharacterLocations charLocations = new CharacterLocations(map);

	private final Game game = new Game("Testlandia", charLocations);

	private final OutputPort jimOut = mock(OutputPort.class);
	private final OutputPort johnOut = mock(OutputPort.class);
	
	@Before
	public void createCharacters() {
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
	}
	
	@Test
	public void scriptTellsCurrentLocationEveryTick() {
		game.addScript(new LocationsTeller());
		
		game.tick();
		
		verify(jimOut).heardFromGame("You're in an open field, County of the Mage.");
		verify(johnOut).heardFromGame("You're in an open field, County of the Mage.");
	}
}
