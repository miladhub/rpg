package rpg.tcp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rpg.game.CharacterLocations;
import rpg.game.Command;
import rpg.game.EnterGame;
import rpg.game.GameContext;
import rpg.game.GameEngine;
import rpg.game.LookAround;
import rpg.game.OutputPort;
import rpg.game.QuitGame;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.Travel;

@RunWith(MockitoJUnitRunner.class)
public class CommandsTest {
	private @Mock OutputPort out = mock(OutputPort.class);
	private @Mock CharacterLocations locs;
	private @Mock GameContext game;
	private Commands commands = new Commands(out);

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
		CharacterCommand cmd = checkCommandClass("go to there", Travel.class);
		cmd.command.execute("jim", new GameEngine(game, locs));
		
		verify(locs).moveToPlace("jim", "there");
	}
	
	private CharacterCommand checkCommandClass(String commandString, Class<? extends Command> clazz) throws UnknownCommandException {
		CharacterCommand cmd = commands.createCharacterCommand(commandString);
		assertTrue(cmd.command.getClass() == clazz);
		return cmd;
	}
}
