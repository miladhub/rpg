package rpg.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorldMap {
	private static final LocalMap DEFAULT_LOCATION_SIZE = new LocalMap(5, 5);
	private final Set<Arch> archs = new HashSet<>();
	private final Set<Location> locs = new HashSet<>();
	private Map<Location, LocalMap> localMaps = new HashMap<>();
	
	private WorldMap() {
		super();
	}

	public void connect(Location first, Location second) {
		locs.add(first);
		locs.add(second);
		archs.add(new Arch(first, second));
	}

	public Set<Location> locationsAdjacentTo(Location loc) {
		Set<Location> adjacentLocs = new HashSet<>();
		for (Arch arch : archs) {
			if (arch.startsFrom(loc)) {
				adjacentLocs.add(arch.end());
			}
			if (arch.endsAt(loc)) {
				adjacentLocs.add(arch.start());
			}
		}
		return adjacentLocs;
	}

	public boolean regionChanged(Location from, Location to) {
		return locationsAreAdjacent(from, to) && !from.region().equals(to.region());
	}

	private boolean locationsAreAdjacent(Location from, Location to) {
		for (Arch arch : archs) {
			if (arch.startsFrom(from) && arch.endsAt(to) || arch.startsFrom(to) && arch.endsAt(from)) {
				return true;
			}
		}
		return false;
	}

	public Location getLocationFrom(Location from, String to) throws CannotGoThereException {
		for (Location adj : locationsAdjacentTo(from)) {
			if (adj.place().equals(to)) {
				return adj;
			}
		}
		throw new CannotGoThereException(to, from);
	}

	public static WorldMap createEmptyMap() {
		return new WorldMap();
	}

	public Set<String> regions() {
		Set<String> regions = new HashSet<>();
		for (Location loc : locs) {
			regions.add(loc.region());
		}
		return regions;
	}

	public Set<Location> locations() {
		return locs;
	}
	
	public static class WorldMapBuilder {
		private Map<String,List<String>> places = new LinkedHashMap<>();
		private String currRegion;
		private String currPlace;
		private Map<String, String> sizes = new HashMap<>();

		public WorldMapBuilder addRegion(String region) {
			currRegion = region;
			places.put(currRegion, new ArrayList<String>());
			return this;
		}

		public WorldMapBuilder addPlace(String place) {
			currPlace = place;
			places.get(currRegion).add(place);
			return this;
		}

		public WorldMapBuilder size(String size) {
			sizes.put(currPlace, size);
			return this;
		}
		
		public WorldMap createMap() {
			WorldMap top = WorldMap.createEmptyMap();
			Location prevLoc = null;
			for (String region : places.keySet()) {
				for (String place : places.get(region)) {
					Location loc = new Location(region, place);
					top.locs.add(loc);
					if (prevLoc != null) {
						top.connect(prevLoc, loc);
					}
					prevLoc = loc;
					if (sizes.containsKey(place)) {
						top.localMaps.put(loc, parseLocalMap(sizes.get(place)));
					}
				}
			}
			return top;
		}

		private LocalMap parseLocalMap(String size) {
			return new LocalMap(Integer.parseInt(size.split("x")[0]), Integer.parseInt(size.split("x")[1]));
		}
	}

	public LocalMap localMap(Location location) {
		if (localMaps.containsKey(location)) {
			return localMaps.get(location);
		} else {
			return DEFAULT_LOCATION_SIZE;
		}
	}
}
