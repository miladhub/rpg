package rpg.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Game implements CommandExecutor, GameContext {
	private final String worldName;
	private final Map<String, OutputPort> outs = new HashMap<>();
	private final ScriptContext actionContext;
	private final List<Script> runningScripts = new ArrayList<>();
	private final Map<String, Script> blockingScripts = new HashMap<>();
	private final Map<Script, Integer> durations = new HashMap<>();
	private final Map<Script, Integer> executionCounters = new HashMap<>();
	private final Map<Script, ScriptSpecification> scriptSpecs = new HashMap<>();

	public Game(String worldName, CharacterLocations charLocations) {
		this.worldName = worldName;
		charLocations.registerToMovements(new RegionChangeNotifier(this));
		actionContext = new GameActionContext(this, charLocations);
	}

	@Override
	public String worldName() {
		return worldName;
	}

	@Override
	public void addCharacter(String character, OutputPort out) {
		outs.put(character, out);
	}
	
	@Override
	public void removeCharacter(String character) {
		outs.remove(character);
	}
	
	@Override
	public OutputPort outputPort(String character) {
		if (character == null) {
			throw new NotInGameException();
		}
		return outs.get(character);
	}
	
	@Override
	public Set<String> characters() {
		return outs.keySet();
	}
	
	public void addScript(Script script) {
		addScript(Scripts.aScript(script));
	}
	
	public void addScript(ScriptSpecificationBuilder specBuilder) {
		ScriptSpecification spec = specBuilder.build();
		runningScripts.add(spec.script());
		scriptSpecs.put(spec.script(), spec);
		if (spec.hasDuration()) {
			durations.put(spec.script(), spec.durationInSeconds());
		}
		executionCounters.put(spec.script(), 0);
	}

	public void tick() {
		for (Script script : new ArrayList<>(runningScripts)) {
			if (mustTick(script)) {
				script.onTick(actionContext);
			}
			checkIfScriptHasFinished(script);
		}
	}

	private boolean mustTick(Script script) {
		if (executionCounters.get(script) == 0) {
			executionCounters.put(script, scriptSpecs.get(script).frequencyInSeconds());
			return true;
		} else {
			executionCounters.put(script, executionCounters.get(script) - 1);
			return false;
		}
	}

	private void checkIfScriptHasFinished(Script script) {
		if (durations.containsKey(script)) {
			durations.put(script, durations.get(script) - 1);
			if (durations.get(script) == 0) {
				durations.remove(script);
				runningScripts.remove(script);
				scriptSpecs.remove(script);
			}
		}
	}

	private void keepBusy(String character, Script script) {
		blockingScripts.put(character, script);		
	}

	public void startScripts() {
		for (Script script : runningScripts) {
			script.onStart(actionContext);
		}
	}

	public void stopScripts() {
		for (Script script : runningScripts) {
			script.onStop(actionContext);
		}		
	}

	private boolean characterIsBusy(String character) {
		return blockingScripts.containsKey(character);
	}

	private void interrupt(String character) {
		if (characterIsBusy(character)) {
			Script interruptedScript = blockingScripts.remove(character);
			interruptedScript.onStop(actionContext);
			durations.remove(interruptedScript);
			runningScripts.remove(interruptedScript);
			scriptSpecs.remove(interruptedScript);
		}
	}

	@Override
	public void execute(Command command) {
		if (!characterIsBusy(command.character())) {
			command.execute(actionContext);
		}
	}
	
	private static class GameActionContext implements ScriptContext {
		private Game game;
		private CharacterLocations charLocations;

		public GameActionContext(Game game, CharacterLocations charLocations) {
			this.game = game;
			this.charLocations = charLocations;
		}

		@Override
		public String worldName() {
			return game.worldName();
		}

		@Override
		public void addCharacter(String character, OutputPort out) {
			game.addCharacter(character, out);
		}

		@Override
		public void removeCharacter(String character) {
			game.removeCharacter(character);
		}

		@Override
		public OutputPort outputPort(String character) {
			return game.outputPort(character);
		}

		@Override
		public CharacterLocations characterLocations() {
			return charLocations;
		}

		@Override
		public Set<String> nearbyCharacters(String character) {
			Set<String> others = new HashSet<>();
			for (String other : characters()) {
				if (!character.equals(other)) {
					Location movingCharLocation = charLocations.whereIs(character);
					Location otherCharLocation = charLocations.whereIs(other);
					if (movingCharLocation.place().equals(otherCharLocation.place())) {
						others.add(other);
					}
				}
			}
			return others;
		}

		@Override
		public Set<String> characters() {
			return game.characters();
		}

		@Override
		public void keepBusy(String character, Script script) {
			game.keepBusy(character, script);
		}

		@Override
		public boolean characterIsBusy(String character) {
			return game.characterIsBusy(character);
		}

		@Override
		public void interrupt(String character) {
			game.interrupt(character);
		}
	}
}
