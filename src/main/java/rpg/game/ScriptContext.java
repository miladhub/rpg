package rpg.game;

public interface ScriptContext extends CommandContext {
	void keepBusy(String character, Script script);
	boolean characterIsBusy(String character);
	void interrupt(String character);
}
