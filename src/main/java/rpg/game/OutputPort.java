package rpg.game;

public interface OutputPort {
	void heardFromGame(String message);
	void heardFrom(String from, String what);
	void sees(String whoOrWhat, LocalPosition where, LocalMap localMap);
	void isAt(LocalPosition localPosition, LocalMap localMap);
	void endSession();
}
