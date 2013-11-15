package rpg.game;

public interface InputPort {
	void say(String what);
	void whereabout();
	void moveTo(String where);
	void whatsNear();
}
