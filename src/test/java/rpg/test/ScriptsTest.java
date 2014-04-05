package rpg.test;

import static rpg.game.Scripts.*;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
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
import rpg.test.support.Tests;

public class ScriptsTest {
	@Rule public final JUnitRuleMockery context = new JUnitRuleMockery();
	
	private final WorldMap map = Tests.testMap().createMap();
	private final CharacterLocations charLocations = new CharacterLocations(map);
	private final Game game = new Game("Testlandia", charLocations);
	
	private @Mock OutputPort jim;
	private @Mock OutputPort john;
	
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
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("Game server is watching you.");
			oneOf(john).heardFromGame("Game server is watching you.");
		}});
		
		game.addScript(new BigBrother());
		game.tick();
	}
	
	@Test
	public void jimSleepsForTwoSeconds() {
		context.checking(new Expectations() {{
			exactly(2).of(jim).heardFromGame("zzz...");
		}});
		
		game.addScript(aScript(new Sleep("jim")).lasting(2));
		game.tick();
		game.tick();
		game.tick();
	}
	
	@Test
	public void poisonActsEveryThreeSeconds() {
		context.checking(new Expectations() {{
			exactly(2).of(jim).heardFromGame("you feel weaker!");
		}});
		
		game.addScript(aScript(new Poison("jim")).lasting(10).tickingEvery(3));
		game.tick();
		game.tick();
		game.tick();
		game.tick();
		game.tick();
		game.tick();
	}
	
	@Test
	public void sleepingKeepsCharacterBusy() {
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("zzz...");
			never(jim).heardFromGame("walking");
		}});
		
		game.addScript(new Sleep("jim"));		
		game.addScript(new Walk("jim"));
		game.tick();
	}
	
	@Test
	public void poisonCreepsWhileSleeping() {
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("zzz...");
			oneOf(jim).heardFromGame("you feel weaker!");
		}});

		game.addScript(new Sleep("jim"));		
		game.addScript(new Poison("jim"));
		game.tick();
	}

	@Test
	public void fightInterruptsSleep() {
		context.checking(new Expectations() {{
			oneOf(jim).heardFromGame("zzz...");
			exactly(2).of(jim).heardFromGame("slash!");
		}});

		game.addScript(new Sleep("jim"));		
		game.addScript(new Fight("jim"));
		game.tick();
		game.tick();
	}
	
	@Test
	public void fightInterruptsSleepThatWasSupposedToLastTenSeconds() {
		context.checking(new Expectations() {{
			atMost(3).of(jim).heardFromGame("zzz...");
			exactly(3).of(jim).heardFromGame("slash!");
		}});

		game.addScript(aScript(new Sleep("jim")).lasting(10));
		game.tick();
		game.tick();
		game.addScript(new Fight("jim"));
		game.tick();
		game.tick();
		game.tick();
	}
	
	@Test
	public void poisonActsWhileSleepingAndLastTwoSeconds() {
		context.checking(new Expectations() {{
			exactly(5).of(jim).heardFromGame("zzz...");
			exactly(2).of(jim).heardFromGame("you feel weaker!");
		}});

		game.addScript(aScript(new Sleep("jim")).lasting(10));		
		game.tick();
		game.tick();
		game.addScript(aScript(new Poison("jim")).lasting(2));
		game.tick();
		game.tick();
		game.tick();
	}
	
	@Test
	public void cannotTalkWhileSleeping() {
		context.checking(new Expectations() {{
			never(jim).heardFrom(with(equal("jim")), with(any(String.class)));
		}});
		
		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		game.addScript(new Sleep("jim"));
		game.execute("jim", new Say("hello"));
	}
	
	@Test
	public void characterCanTalkAfterSleep() {
		
		context.checking(new Expectations() {{
			allowing(jim).heardFromGame("zzz...");
			
			oneOf(john).heardFrom("jim", "hello");
		}});

		charLocations.setCharacterAtLocation("jim", "County of the Mage", "an open field");
		charLocations.setCharacterAtLocation("john", "County of the Mage", "an open field");
		
		game.addScript(Scripts.aScript(new Sleep("jim")).lasting(2));
		game.tick();
		game.tick();
		game.tick();
		game.execute("jim", new Say("hello"));
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
