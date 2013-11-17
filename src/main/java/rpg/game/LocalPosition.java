package rpg.game;

public class LocalPosition {
	public final int x;
	public final int y;

	public LocalPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "x = " + x + ", y = " + y;
	}
}
