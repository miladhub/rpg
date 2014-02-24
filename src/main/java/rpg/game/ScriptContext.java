package rpg.game;

public interface ScriptContext extends CommandContext {
	void keepBusy(String character, Script script);
	boolean characterIsBusy(String string);
	void interrupt(String character);
}
