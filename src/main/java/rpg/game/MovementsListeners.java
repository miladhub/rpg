package rpg.game;

import java.util.ArrayList;
import java.util.List;

public class MovementsListeners {
	private final List<MovementsListener> listeners = new ArrayList<>();
	
	public void add(MovementsListener listener) {
		listeners.add(listener);
	}

	public void notifyRegionChanged(String character, String region) {
		for (MovementsListener l : listeners) {
			l.regionChangedTo(character, region);
		}
	}
}
