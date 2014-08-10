package rpg.game;

public class Fight extends BaseScript {
	private String character;

	public Fight(String character) {
		this.character = character;
	}

	@Override
	public void onTick(ScriptContext context) {
		context.interrupt(character);
		context.character(character).heardFromGame("slash!");
	}
}