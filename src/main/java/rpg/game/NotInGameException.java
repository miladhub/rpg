package rpg.game;

public class NotInGameException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotInGameException() {
		super("Not in game");
	}
}
