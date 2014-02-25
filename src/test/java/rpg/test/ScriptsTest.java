package rpg.test;

import static org.mockito.Mockito.*;
import static rpg.game.Scripts.*;

import org.junit.Before;
import org.junit.Test;

import rpg.game.CharacterLocations;
import rpg.game.Game;
import rpg.game.OutputPort;
import rpg.game.Script;
import rpg.game.ScriptContext;
import rpg.game.WorldMap;

public class ScriptsTest {
	private final Game game = new Game("Testlandia", new CharacterLocations(WorldMap.createEmptyMap()));
	private final OutputPort jim = mock(OutputPort.class);
	private final OutputPort john = mock(OutputPort.class);
	
	@Before
	public void createCharacters() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
	}
	
	@Test
	public void scriptSpeaksToCharactersEveryTick() {
		game.addScript(new BigBrother());
		
		game.tick();
		
		verify(jim).heardFromGame("Game server is watching you.");
		verify(john).heardFromGame("Game server is watching you.");
	}
	
	@Test
	public void jimSleepsForTwoSeconds() {
		game.addScript(aScript(new Sleep("jim")).lasting(2));
		
		game.tick();
		game.tick();
		game.tick();
		
		verify(jim, times(2)).heardFromGame("zzz...");
	}
	
	@Test
	public void poisonActsEveryThreeSeconds() {
		game.addScript(aScript(new Poison("jim")).lasting(10).every(3));
		
		game.tick();
		game.tick();
		game.tick();
		game.tick();
		game.tick();
		game.tick();
		
		verify(jim, times(2)).heardFromGame("you feel weaker!");
	}
	
	@Test
	public void sleepingKeepsCharacterBusy() {
		game.addScript(new Sleep("jim"));		
		game.addScript(new Walk("jim"));
		
		game.startScripts();
		game.tick();
		game.stopScripts();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim, never()).heardFromGame("walking");
	}
	
	@Test
	public void poisonCreepsWhileSleeping() {
		game.addScript(new Sleep("jim"));		
		game.addScript(new Poison("jim"));
		
		game.startScripts();
		game.tick();
		game.stopScripts();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim).heardFromGame("you feel weaker!");
	}

	@Test
	public void fightInterruptsSleep() {
		game.addScript(new Sleep("jim"));		
		game.addScript(new Fight("jim"));
		
		game.startScripts();
		game.tick();
		game.tick();
		game.stopScripts();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim, times(2)).heardFromGame("slash!");
	}
	
	@Test
	public void fightInterruptsSleepThatWasSupposedToLastTenSeconds() {
		game.addScript(aScript(new Sleep("jim")).lasting(10));
		
		game.startScripts();
		game.tick();
		game.tick();
		game.addScript(new Fight("jim"));
		game.tick();
		game.tick();
		game.tick();
		game.stopScripts();
		
		verify(jim, atMost(3)).heardFromGame("zzz...");
		verify(jim, times(3)).heardFromGame("slash!");
	}
	
	@Test
	public void poisonActsWhileSleepingAndLastTwoSeconds() {
		game.addScript(aScript(new Sleep("jim")).lasting(10));		
		
		game.startScripts();
		game.tick();
		game.tick();
		game.addScript(aScript(new Poison("jim")).lasting(2));
		game.tick();
		game.tick();
		game.tick();
		game.stopScripts();
		
		verify(jim, times(5)).heardFromGame("zzz...");
		verify(jim, times(2)).heardFromGame("you feel weaker!");
	}
	
	private static abstract class BaseScript implements Script {
		@Override
		public void onStart(ScriptContext context) {}

		@Override
		public void onStop(ScriptContext context) {}
	}
	
	private static class Fight extends BaseScript {
		private String character;

		public Fight(String character) {
			this.character = character;
		}
		
		@Override
		public void onTick(ScriptContext context) {
			context.interrupt(character);
			context.outputPort(character).heardFromGame("slash!");
		}
	}
	
	private static class Poison extends BaseScript {
		private String character;

		public Poison(String character) {
			this.character = character;
		}
		
		@Override
		public void onTick(ScriptContext context) {
			context.outputPort(character).heardFromGame("you feel weaker!");
		}
	}
	
	private static class Walk extends BaseScript {
		private String character;

		public Walk(String character) {
			this.character = character;
		}

		@Override
		public void onTick(ScriptContext context) {
			if (!context.characterIsBusy(character)) {
				context.outputPort(character).heardFromGame("walking");
			}
		}
	}

	private static class BigBrother extends BaseScript {
		@Override
		public void onTick(ScriptContext context) {
			for (String character : context.characters()) {
				context.outputPort(character).heardFromGame(
						"Game server is watching you.");
			}
		}
	}

	private static class Sleep extends BaseScript {
		private String character;

		public Sleep(String character) {
			this.character = character;
		}

		@Override
		public void onStart(ScriptContext context) {
			context.keepBusy(character, this);
		}
		
		@Override
		public void onTick(ScriptContext context) {
			context.outputPort(character).heardFromGame("zzz...");
		}
	}
}
