package rpg.game;

public interface OutputPort {
	void heardFromGame(String message);
	void heardFrom(String from, String what);
	void sees(String whoOrWhat);
	void movedTo(LocalPosition localPosition);
	void endSession();
}
