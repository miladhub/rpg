package rpg.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import rpg.game.Game;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.LookAround;
import rpg.game.NotInGameException;
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
	
	@Test(expected = NotInGameException.class)
	public void executingCommandWithoutCharacterEndsGame() {
		game.outputPort(null);
	}
	
	@Test
	public void jimSpeaksToJohn() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		game.execute(new Say("jim", "hi"));
		verify(johnOut).heardFrom("jim", "hi");
		verify(jimOut, never()).heardFrom(same("jim"), anyString());
	}
	
	@Test
	public void jimSpeaksToKenWhosTooFar() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		OutputPort kenOut = mock(OutputPort.class);
		game.addCharacter("ken", kenOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("ken", "the County of the Warrior", "the Warrior border");
		
		game.execute(new Say("jim", "hi"));
		verify(kenOut, never()).heardFrom(same("jim"), anyString());
	}
	
	
	@Test
	public void jimAsksForHisWhereabout() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		game.execute(new TellWhereabout("jim"));
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
		
		game.execute(new Travel("john", "an open field"));
		verify(jimOut).sees("john", new LocalPosition(0, 0), new LocalMap(5, 5));
		verify(johnOut).sees("jim", new LocalPosition(0, 0), new LocalMap(5, 5));
	}
	
	@Test
	public void jimDiscoversAdjacentField() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");

		game.execute(new TellWhatsNear("jim"));
		
		verify(jimOut).heardFromGame("You can go to:");
		verify(jimOut).heardFromGame("\ta field next to the previous one");
	}
	
	@Test
	public void jimCrossesTheBorder() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "the Mage border");
		
		game.execute(new Travel("jim", "the Warrior border"));
		
		verify(jimOut).heardFromGame("You have crossed into the County of the Warrior.");
	}
	
	@Test
	public void jimStaysWithinTheRegion() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		game.execute(new Travel("jim", "a field next to the previous one"));
		
		verify(jimOut, never()).heardFromGame("You have crossed into County of the Mage.");
	}
	
	@Test
	public void youCantSkipPlaces() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		game.execute(new Travel("jim", "the Mage border"));
		verify(jimOut).heardFromGame("You can't travel to the Mage border from your current location.");
	}
	
	@Test
	public void tellPosition() {
		joinJim();
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		game.execute(new TellPosition("jim"));

		verify(jimOut).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
	}
	
	@Test
	public void jimAndJohnSeeEachOtherInTheLocalMap() {
		game.addCharacter("jim", jimOut);
		game.addCharacter("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		charLocations.setLocalPosition("john", 2, 2);
		
		game.execute(new LookAround("jim"));
		verify(jimOut).sees("john", new LocalPosition(2, 2), new LocalMap(5, 5));
		
		game.execute(new LookAround("john"));
		verify(johnOut).sees("jim", new LocalPosition(0, 0), new LocalMap(5, 5));
	}
}
