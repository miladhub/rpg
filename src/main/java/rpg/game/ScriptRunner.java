package rpg.game;

public interface ScriptRunner {
	void addScript(ScriptSpecificationBuilder specBuilder);
	void tick();
	void startScripts();
	void stopScripts();
}
