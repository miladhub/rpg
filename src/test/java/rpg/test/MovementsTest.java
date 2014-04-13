package rpg.test;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rpg.game.Direction;
import rpg.game.Game;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.Move;
import rpg.game.CharacterHandle;
import rpg.game.TellPosition;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;
import rpg.test.support.Tests;

public class MovementsTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final WorldMap map = Tests.testMap().createMap();
	private final CharacterLocations charLocations = new CharacterLocations(map);
	private final Game game = new Game("Testlandia", charLocations);
	
	private @Mock CharacterHandle jimOut;

	@Before
	public void joinJim() {
		game.addCharacter("jim", jimOut);
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
	}
	
	@Test
	public void moveForward() {
		charLocations.setLocalPosition("jim", 0, 0);
		
		context.checking(new Expectations() {{
			exactly(2).of(jimOut).isAt(new LocalPosition(0, 1), new LocalMap(5, 5));
		}});

		game.execute("jim", new Move(Direction.Forward));
		game.execute("jim", new TellPosition());
	}
	
	@Test
	public void moveBackward() {
		charLocations.setLocalPosition("jim", 0, 1);
		
		context.checking(new Expectations() {{
			exactly(2).of(jimOut).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
		}});
		
		game.execute("jim", new Move(Direction.Backward));
		game.execute("jim", new TellPosition());
	}
	
	@Test
	public void moveLeft() {
		charLocations.setLocalPosition("jim", 1, 0);
		
		context.checking(new Expectations() {{
			exactly(2).of(jimOut).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
		}});
		
		game.execute("jim", new Move(Direction.Left));
		game.execute("jim", new TellPosition());
	}
	
	@Test
	public void moveRight() {
		charLocations.setLocalPosition("jim", 0, 0);
		
		context.checking(new Expectations() {{
			exactly(2).of(jimOut).isAt(new LocalPosition(1, 0), new LocalMap(5, 5));
		}});
		
		game.execute("jim", new Move(Direction.Right));
		game.execute("jim", new TellPosition());
	}
}
