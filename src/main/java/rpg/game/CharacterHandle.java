package rpg.game;

public interface CharacterHandle {
	void heardFromGame(String message);
	void heardFrom(String from, String what);
	void sees(String whoOrWhat, LocalPosition where, LocalMap localMap);
	void isAt(LocalPosition localPosition, LocalMap localMap);
	void endSession();
}
