package rpg.game;

public interface Script {
	void onStart(ScriptContext context);
	void onTick(ScriptContext context);
	void onStop(ScriptContext context);
}
