package rpg.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Game implements CommandExecutor, GameContext {
	private final String worldName;
	private final Map<String, OutputPort> outs = new HashMap<>();
	private final GameEngine actionContext;

	public Game(String worldName, CharacterLocations charLocations) {
		this.worldName = worldName;
		charLocations.registerToMovements(new RegionChangeNotifier(this));
		actionContext = new GameEngine(this, charLocations);
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
	
	@Override
	public void execute(String character, Command command) {
		if (!actionContext.characterIsBusy(character)) {
			command.execute(character, actionContext);
		}
	}
	
	public void addScript(Script script) {
		actionContext.addScript(Scripts.aScript(script));
	}

	public void addScript(ScriptSpecificationBuilder specBuilder) {
		actionContext.addScript(specBuilder);
	}

	public void tick() {
		actionContext.tick();
	}

	public void stopScripts() {
		actionContext.stopScripts();
	}
}
