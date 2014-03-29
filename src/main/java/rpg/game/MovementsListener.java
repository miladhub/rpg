package rpg.game;

public interface MovementsListener {
	void regionChangedTo(String character, String region);
	void positionChangedTo(String character, LocalPosition localPosition, LocalMap localMap);
}
