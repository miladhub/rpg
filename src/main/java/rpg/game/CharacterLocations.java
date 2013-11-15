package rpg.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CharacterLocations {
	private final Map<String, Location> charsLocations = new HashMap<>();
	private final WorldMap map;
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
}
