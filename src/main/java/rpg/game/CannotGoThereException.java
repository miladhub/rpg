package rpg.game;

public class CannotGoThereException extends Exception {
	private static final long serialVersionUID = 1L;

	public CannotGoThereException(String to, Location from) {
		super(to + " is not adjacent to " + from);
	}
}
