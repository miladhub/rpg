package rpg.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import rpg.game.Direction;
import rpg.game.Game;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.LookAround;
import rpg.game.Move;
import rpg.game.Say;
import rpg.game.OutputPort;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.TellWhereabout;
import rpg.game.Travel;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;

public class GameTest {
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

	private void joinJim() {
		game.addCharacter("jim", jimOut);
	}
	
	@Test
	public void worldName() {
		assertEquals("Testlandia", game.worldName());
	}
	
	@Test
	public void enteringGameGreetsPlayer() {
		game.addCharacter("jim", jimOut);
	}
	
	@Test
	public void jimSpeaksToJohn() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		new Say("jim", "hi").execute(game);
		verify(johnOut).heardFrom("jim", "hi");
		verify(jimOut, never()).heardFrom(same("jim"), anyString());
	}
	
	@Test
	public void jimAsksForHisWhereabout() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		new TellWhereabout("jim").execute(game);
		verify(jimOut).heardFromGame("You're in an open field, County of the Mage.");
	}
	
	@Test
	public void jimSeesJohnApproaching() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "a field next to the previous one");
		charLocations.setLocalPosition("jim", 0, 0);
		charLocations.setLocalPosition("john", 0, 0);
		
		new Travel("john", "an open field").execute(game);
		verify(jimOut).sees("john", new LocalPosition(0, 0));
		verify(johnOut).sees("jim", new LocalPosition(0, 0));
	}
	
	@Test
	public void jimDiscoversAdjacentField() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");

		new TellWhatsNear("jim").execute(game);
		
		verify(jimOut).heardFromGame("You can go to:");
		verify(jimOut).heardFromGame("\ta field next to the previous one");
	}
	
	@Test
	public void jimCrossesTheBorder() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "the Mage border");
		
		new Travel("jim", "the Warrior border").execute(game);
		
		verify(jimOut).heardFromGame("You have crossed into the County of the Warrior.");
	}
	
	@Test
	public void jimStaysWithinTheRegion() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		new Travel("jim", "a field next to the previous one").execute(game);
		
		verify(jimOut, never()).heardFromGame("You have crossed into County of the Mage.");
	}
	
	@Test
	public void tellPosition() {
		joinJim();
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		new TellPosition("jim").execute(game);

		verify(jimOut).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
	}
	
	@Test
	public void moveForward() {
		joinJim();
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		new Move("jim", Direction.Forward).execute(game);
		new TellPosition("jim").execute(game);

		verify(jimOut, times(2)).isAt(new LocalPosition(0, 1), new LocalMap(5, 5));
	}
	
	@Test
	public void jimAndJohnSeeEachOtherInTheLocalMap() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		charLocations.setLocalPosition("john", 2, 2);
		
		new LookAround("jim").execute(game);
		verify(jimOut).sees("john", new LocalPosition(2, 2));
		
		new LookAround("john").execute(game);
		verify(johnOut).sees("jim", new LocalPosition(0, 0));
	}
}
