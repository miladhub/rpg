package rpg.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CharacterLocations {
	private final WorldMap map;
	private final Map<String, Location> charsLocations = new HashMap<>();
	private final Map<String, LocalPosition> positions = new HashMap<>();
	private final MovementsListeners listeners = new MovementsListeners();
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((charsLocations == null) ? 0 : charsLocations.hashCode());
		result = prime * result
				+ ((positions == null) ? 0 : positions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterLocations other = (CharacterLocations) obj;
		if (charsLocations == null) {
			if (other.charsLocations != null)
				return false;
		} else if (!charsLocations.equals(other.charsLocations))
			return false;
		if (positions == null) {
			if (other.positions != null)
				return false;
		} else if (!positions.equals(other.positions))
			return false;
		return true;
	}
}
