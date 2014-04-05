package rpg.test;

import static org.junit.Assert.*;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import rpg.game.CharacterLocations;
import rpg.game.Command;
import rpg.game.EnterGame;
import rpg.game.Game;
import rpg.game.GameEngine;
import rpg.game.LookAround;
import rpg.game.QuitGame;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.Travel;
import rpg.game.WorldMap;
import rpg.tcp.CharacterCommand;
import rpg.tcp.Commands;
import rpg.tcp.UnknownCommandException;

public class CommandsTest {
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
	private final Commands commands = new Commands(null);

	@Test
	public void createEnter() throws Exception {
		CharacterCommand cmd = checkCommandClass("enter as jim", EnterGame.class);
		assertEquals("jim", cmd.character);
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
		CharacterCommand cmd = checkCommandClass("go to a field next to the previous one", Travel.class);
		cmd.command.execute("jim", new GameEngine(game, charLocations));
	}

	private CharacterCommand checkCommandClass(String commandString, Class<? extends Command> clazz) throws UnknownCommandException {
		CharacterCommand cmd = commands.createCharacterCommand(commandString);
		assertTrue(cmd.command.getClass() == clazz);
		return cmd;
	}
}
