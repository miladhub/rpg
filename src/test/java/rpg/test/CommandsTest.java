package rpg.test;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import rpg.game.*;
import rpg.tcp.Commands;
import rpg.tcp.UnknownCommandException;

public class CommandsTest implements ClientState {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();

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
	private final Commands commands = new Commands(null, this);
	private String character;

	@Test
	public void createEnter() throws Exception {
		checkCommandClass("enter as jim", EnterGame.class);
		assertEquals("jim", character);
	}

	@Test
	public void createQuit() throws Exception {
		checkCommandClass("quit", QuitGame.class);
	}

	@Test
	public void createWhatsnear() throws Exception {
		checkCommandClass("near", TellWhatsNear.class);
	}

	@Test
	public void createPosition() throws Exception {
		checkCommandClass("position", TellPosition.class);
	}

	@Test
	public void createLook() throws Exception {
		checkCommandClass("look", LookAround.class);
	}

	@Test
	public void createTravel() throws Exception {
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		Command cmd = checkCommandClass("go to a field next to the previous one", Travel.class);
		cmd.execute("jim", new GameEngine(game, charLocations));
	}

	private Command checkCommandClass(String commandString, Class<? extends Command> clazz) throws UnknownCommandException {
		Command cmd = commands.parseCommand(commandString);
		assertTrue(cmd.getClass() == clazz);
		return cmd;
	}

	@Override
	public void setCharacter(String character) {
		this.character = character;
	}
}
