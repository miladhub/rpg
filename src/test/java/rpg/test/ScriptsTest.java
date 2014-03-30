package rpg.test;

import static org.mockito.Mockito.*;
import static rpg.game.Scripts.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import rpg.game.BaseScript;
import rpg.game.CharacterLocations;
import rpg.game.Game;
import rpg.game.OutputPort;
import rpg.game.Say;
import rpg.game.ScriptContext;
import rpg.game.Scripts;
import rpg.game.Sleep;
import rpg.game.WorldMap;

public class ScriptsTest {
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
	private final OutputPort jim = mock(OutputPort.class);
	private final OutputPort john = mock(OutputPort.class);
	
	@Before
	public void createCharacters() {
		game.addCharacter("jim", jim);
		game.addCharacter("john", john);
	}
	
	@After
	public void stopScripts() {
		game.stopScripts();
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
		game.addScript(aScript(new Poison("jim")).lasting(10).tickingEvery(3));
		
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
		
		game.tick();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim, never()).heardFromGame("walking");
	}
	
	@Test
	public void poisonCreepsWhileSleeping() {
		game.addScript(new Sleep("jim"));		
		game.addScript(new Poison("jim"));
		
		game.tick();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim).heardFromGame("you feel weaker!");
	}

	@Test
	public void fightInterruptsSleep() {
		game.addScript(new Sleep("jim"));		
		game.addScript(new Fight("jim"));
		
		game.tick();
		game.tick();
		
		verify(jim).heardFromGame("zzz...");
		verify(jim, times(2)).heardFromGame("slash!");
	}
	
	@Test
	public void fightInterruptsSleepThatWasSupposedToLastTenSeconds() {
		game.addScript(aScript(new Sleep("jim")).lasting(10));
		
		game.tick();
		game.tick();
		game.addScript(new Fight("jim"));
		game.tick();
		game.tick();
		game.tick();
		
		verify(jim, atMost(3)).heardFromGame("zzz...");
		verify(jim, times(3)).heardFromGame("slash!");
	}
	
	@Test
	public void poisonActsWhileSleepingAndLastTwoSeconds() {
		game.addScript(aScript(new Sleep("jim")).lasting(10));		
		
		game.tick();
		game.tick();
		game.addScript(aScript(new Poison("jim")).lasting(2));
		game.tick();
		game.tick();
		game.tick();
		
		verify(jim, times(5)).heardFromGame("zzz...");
		verify(jim, times(2)).heardFromGame("you feel weaker!");
	}
	
	@Test
	public void cannotTalkWhileSleeping() {
		game.addScript(new Sleep("jim"));
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		game.execute("jim", new Say("hello"));
		
		verify(john, never()).heardFrom(eq("jim"), anyString());
	}
	
	@Test
	public void characterCanTalkAfterSleep() {
		game.addScript(Scripts.aScript(new Sleep("jim")).lasting(2));
		
		game.tick();
		game.tick();
		game.tick();
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		game.execute("jim", new Say("hello"));
		
		verify(john).heardFrom("jim", "hello");
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
}
