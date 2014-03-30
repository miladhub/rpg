package rpg.test;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import rpg.game.Direction;
import rpg.game.Game;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.Move;
import rpg.game.OutputPort;
import rpg.game.TellPosition;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;

public class MovementsTest {
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

	@Before
	public void joinJim() {
		game.addCharacter("jim", jimOut);
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
	}
	
	@Test
	public void moveForward() {
		charLocations.setLocalPosition("jim", 0, 0);
		game.execute("jim", new Move(Direction.Forward));
		game.execute("jim", new TellPosition());
		verify(jimOut, times(2)).isAt(new LocalPosition(0, 1), new LocalMap(5, 5));
	}
	
	@Test
	public void moveBackward() {
		charLocations.setLocalPosition("jim", 0, 1);
		game.execute("jim", new Move(Direction.Backward));
		game.execute("jim", new TellPosition());
		verify(jimOut, times(2)).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
	}
	
	@Test
	public void moveLeft() {
		charLocations.setLocalPosition("jim", 1, 0);
		game.execute("jim", new Move(Direction.Left));
		game.execute("jim", new TellPosition());
		verify(jimOut, times(2)).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
	}
	
	@Test
	public void moveRight() {
		charLocations.setLocalPosition("jim", 0, 0);
		game.execute("jim", new Move(Direction.Right));
		game.execute("jim", new TellPosition());
		verify(jimOut, times(2)).isAt(new LocalPosition(1, 0), new LocalMap(5, 5));
	}
}
