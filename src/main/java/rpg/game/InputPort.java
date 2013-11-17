package rpg.game;

public interface InputPort {
	String character();
	void say(String what);
	void whereabout();
	void moveTo(String where);
	void whatsNear();
	void move(Direction dir);
	void position();
}
