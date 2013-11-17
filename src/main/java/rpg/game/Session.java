package rpg.game;

public class Session implements InputPort {
	private final String character;
	private final Game game;

	public Session(Game game, String character) {
		this.game = game;
		this.character = character;
	}
	
	@Override
	public String character() {
		return character;
	}

	@Override
	public void say(String what) {
		new Say(character, what).execute(game);
	}

	@Override
	public void whereabout() {
		new TellWhereabout(character).execute(game);
	}

	@Override
	public void moveTo(String where) {
		new Travel(character, where).execute(game);
	}

	@Override
	public void whatsNear() {
		new TellWhatsNear(character).execute(game);
	}

	@Override
	public void move(Direction dir) {
		new Move(character, dir).execute(game);
	}

	@Override
	public void position() {
		new TellPosition(character).execute(game);
	}
}
