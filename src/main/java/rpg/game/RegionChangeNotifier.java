package rpg.game;

public class RegionChangeNotifier implements MovementsListener {
	private final GameContext game;

	public RegionChangeNotifier(GameContext game) {
		this.game = game;
	}

	@Override
	public void regionChangedTo(String character, String region) {
		game.character(character).heardFromGame("You have crossed into " + region + ".");
	}

	@Override
	public void positionChangedTo(String character, LocalPosition localPosition, LocalMap localMap) {
		game.character(character).isAt(localPosition, localMap);
	}
}
