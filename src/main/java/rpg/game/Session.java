package rpg.game;

public class Session implements InputPort {
	private final String character;
	private final Game game;

	public Session(Game game, String character) {
		this.game = game;
		this.character = character;
	}

	@Override
	public void say(String what) {
		game.said(character, what);
	}

	@Override
	public void whereabout() {
		game.tellWhereaboutOf(character);
	}

	@Override
	public void moveTo(String where) {
		game.moved(character, where);
	}

	@Override
	public void whatsNear() {
		game.tellWhatsNear(character);
	}
}
