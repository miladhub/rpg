package rpg.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import rpg.game.Direction;
import rpg.game.Game;
import rpg.game.InputPort;
import rpg.game.OutputPort;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;

public class GameTest {
	private final WorldMap map = new WorldMap.WorldMapBuilder()
			.addRegion("County of the Mage")
			.addPlace("an open field")
			.addPlace("a field next to the previous one")
			.addPlace("the Mage border")
			.addRegion("the County of the Warrior")
			.addPlace("the Warrior border")
			.createMap();
	
	private final CharacterLocations charLocations = new CharacterLocations(map);
	
	private final Game game = new Game("Testlandia", charLocations);
	
	private final OutputPort jimOut = mock(OutputPort.class);
	private final OutputPort johnOut = mock(OutputPort.class);

	private InputPort joinJim() {
		return game.enterAs("jim", jimOut);
	}
	
	@Test
	public void worldName() {
		assertEquals("Testlandia", game.worldName());
	}
	
	@Test
	public void enteringGameGreetsPlayer() {
		game.enterAs("jim", jimOut);
		verify(jimOut).heardFromGame("Welcome to Testlandia, jim!");
	}
	
	@Test
	public void jimSpeaksToJohn() {
		InputPort jimIn = game.enterAs("jim", jimOut);
		
		game.enterAs("john", johnOut);
		
		jimIn.say("hi");
		verify(johnOut).heardFrom("jim", "hi");
		verify(jimOut, never()).heardFrom(same("jim"), anyString());
	}
	
	@Test
	public void jimAsksForHisWhereabout() {
		InputPort jim = joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		jim.whereabout();
		verify(jimOut).heardFromGame("You're in an open field, County of the Mage.");
	}
	
	@Test
	public void jimSeesJohnApproaching() {
		game.enterAs("jim", jimOut);
		InputPort john = game.enterAs("john", johnOut);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "a field next to the previous one");

		john.moveTo("an open field");
		verify(jimOut).sees("john");
		verify(johnOut).sees("jim");
	}
	
	@Test
	public void jimDiscoversAdjacentField() {
		InputPort jim = joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");

		jim.whatsNear();
		
		verify(jimOut).heardFromGame("You can go to:");
		verify(jimOut).heardFromGame("\ta field next to the previous one");
	}
	
	@Test
	public void jimCrossesTheBorder() {
		InputPort jim = joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "the Mage border");
		
		jim.moveTo("the Warrior border");
		
		verify(jimOut).heardFromGame("You have crossed into the County of the Warrior.");
	}
	
	@Test
	public void jimStaysWithinTheRegion() {
		InputPort jim = joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		jim.moveTo("a field next to the previous one");
		
		verify(jimOut, never()).heardFromGame("You have crossed into County of the Mage.");
	}
	
	@Test
	public void moveForward() {
		InputPort jim = joinJim();
		verify(jimOut).heardFromGame("Welcome to Testlandia, jim!");
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		jim.move(Direction.Forward);
		verify(jimOut).heardFromGame("Local position is now x = 0, y = 1.");

		jim.position();
		verify(jimOut).heardFromGame("You're at position x = 0, y = 1.");
	}
	
	//TODO: visualize grid
	//TODO: read from file
}
