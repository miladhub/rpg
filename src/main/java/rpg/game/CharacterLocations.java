package rpg.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CharacterLocations {
	private final Map<String, Location> charsLocations = new HashMap<>();
	private final WorldMap map;
	private final MovementsListeners listeners = new MovementsListeners();
	private Map<String, LocalPosition> positions = new HashMap<>();
	
	public CharacterLocations(WorldMap map) {
		super();
		this.map = map;
	}

	public void setCharacterAtLocation(String character, String region, String place) {
		charsLocations.put(character, new Location(region, place));
	}

	public void moveToPlace(String movingChar, String where) throws CannotGoThereException {
		Location whereWas = whereIs(movingChar);
		Location whereIs = map.getLocationFrom(whereWas, where);
		charsLocations.put(movingChar, whereIs);
		if (map.regionChanged(whereWas, whereIs)) {
			listeners.notifyRegionChanged(movingChar, whereIs(movingChar).region());
		}
	}

	public Location whereIs(String character) {
		return charsLocations.get(character);
	}

	public Set<Location> locationsAdjacentTo(String character) {
		return map.locationsAdjacentTo(whereIs(character));
	}

	public void registerToMovements(MovementsListener listener) {
		listeners.add(listener);
	}

	public void setLocalPosition(String character, int x, int y) {
		positions.put(character, new LocalPosition(x, y));
	}

	public LocalPosition localPosition(String character) {
		return positions.get(character);
	}

	public void move(String character, Direction dir) {
		LocalPosition pos = localPosition(character);
		switch (dir) {
		case Backward:
			positions.put(character, new LocalPosition(pos.x, pos.y - 1));
			break;
		case Forward:
			positions.put(character, new LocalPosition(pos.x, pos.y + 1));
			break;
		case Left:
			positions.put(character, new LocalPosition(pos.x - 1, pos.y));
			break;
		case Right:
			positions.put(character, new LocalPosition(pos.x + 1, pos.y));
			break;
		}
		listeners.notifyPositionChanged(character, localPosition(character));
	}

	public LocalMap localMap(String character) {
		return map.localMap(whereIs(character));
	}
}
