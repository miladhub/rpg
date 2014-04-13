package rpg.game;

public class Sleep extends BaseScript {
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
		context.character(character).heardFromGame("zzz...");
	}
}
