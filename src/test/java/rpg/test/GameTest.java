package rpg.test;

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import rpg.game.Game;
import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.game.LookAround;
import rpg.game.NotInGameException;
import rpg.game.Say;
import rpg.game.CharacterHandle;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.TellWhereabout;
import rpg.game.Travel;
import rpg.game.WorldMap;
import rpg.game.CharacterLocations;
import rpg.test.support.Tests;

public class GameTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final WorldMap map = Tests.testMap().createMap();
	private final CharacterLocations charLocations = new CharacterLocations(map);
	private final Game game = new Game("Testlandia", charLocations);
	
	private @Mock CharacterHandle jim, john;

	private void joinJim() {
		game.addCharacter("jim", jim);
	}
	
	@Test
	public void worldName() {
		assertEquals("Testlandia", game.worldName());
	}
	
	@Test
	public void enteringGameGreetsPlayer() {
		game.addCharacter("jim", jim);
	}
	
	@Test(expected = NotInGameException.class)
	public void executingCommandWithoutCharacterEndsGame() {
		game.character(null);
	}
	
	@Test
	public void jimSpeaksToJohn() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		context.checking(new Expectations() {{
			oneOf(john).heardFrom("jim", "hi");
			never(jim).heardFrom(with(same("jim")), with(any(String.class)));
		}});

		game.execute("jim", new Say("hi"));
	}
	
	@Test
	public void jimSpeaksToKenWhosTooFar() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
		final CharacterHandle ken = context.mock(CharacterHandle.class);
		game.addCharacter("ken", ken);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("ken", "the County of the Warrior", "the Warrior border");
		
		context.checking(new Expectations() {{
			allowing(jim);
			allowing(john);
			
			never(ken).heardFrom(with(equal("jim")), with(any(String.class)));
		}});
		
		game.execute("jim", new Say("hi"));
	}
	
	
	@Test
	public void jimAsksForHisWhereabout() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");

		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("You're in an open field, County of the Mage.");
		}});
		
		game.execute("jim", new TellWhereabout());
	}
	
	@Test
	public void jimSeesJohnApproaching() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "a field next to the previous one");
		charLocations.setLocalPosition("jim", 0, 0);
		charLocations.setLocalPosition("john", 0, 0);
		
		context.checking(new Expectations() {{
			oneOf(jim).sees("john", new LocalPosition(0, 0), new LocalMap(5, 5));
			oneOf(john).sees("jim", new LocalPosition(0, 0), new LocalMap(5, 5));
		}});
		
		game.execute("john", new Travel("an open field"));
	}
	
	@Test
	public void jimDiscoversAdjacentField() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("You can go to:");
			oneOf(jim).heardFromGame("\ta field next to the previous one");
		}});
		
		game.execute("jim", new TellWhatsNear());
	}
	
	@Test
	public void jimCrossesTheBorder() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "the Mage border");
		
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("You have crossed into the County of the Warrior.");
		}});
		
		game.execute("jim", new Travel("the Warrior border"));
	}
	
	@Test
	public void jimStaysWithinTheRegion() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		context.checking(new Expectations() {{
			never(jim).heardFromGame("You have crossed into County of the Mage.");
		}});
		
		game.execute("jim", new Travel("a field next to the previous one"));
	}
	
	@Test
	public void youCantSkipPlaces() {
		joinJim();
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("You can't travel to the Mage border from your current location.");
		}});
		
		game.execute("jim", new Travel("the Mage border"));
	}
	
	@Test
	public void tellPosition() {
		joinJim();
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		context.checking(new Expectations() {{
			oneOf(jim).isAt(new LocalPosition(0, 0), new LocalMap(5, 5));
		}});
		
		game.execute("jim", new TellPosition());
	}
	
	@Test
	public void jimAndJohnSeeEachOtherInTheLocalMap() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setLocalPosition("jim", 0, 0);
		
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		charLocations.setLocalPosition("john", 2, 2);
		
		context.checking(new Expectations() {{
			oneOf(jim).sees("john", new LocalPosition(2, 2), new LocalMap(5, 5));
			oneOf(john).sees("jim", new LocalPosition(0, 0), new LocalMap(5, 5));
		}});
		
		game.execute("jim", new LookAround());
		game.execute("john", new LookAround());
	}
}
