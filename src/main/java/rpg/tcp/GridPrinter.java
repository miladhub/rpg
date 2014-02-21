package rpg.tcp;

import rpg.game.LocalMap;
import rpg.game.LocalPosition;

public class GridPrinter {
	private static final String STEP = "       ";
	
	private final LocalPosition lp;
	private final LocalMap localMap;

	public GridPrinter(LocalPosition localPosition, LocalMap localMap) {
		this.lp = localPosition;
		this.localMap = localMap;
	}

	public String print(String whoOrWhat) {
		if (whoOrWhat.length() > 3) {
			whoOrWhat = whoOrWhat.substring(0, 3);
		}
		String show = "";
		for (int row = localMap.height() - 1; row >= 0; row--) {
			if (row != lp.y) {
				for (int col = 0; col < localMap.width(); col++) {
					show += "|" + STEP;
				}
				show += "|\n";
			} else {
				for (int col = 0; col < localMap.width(); col++) {
					if (col != lp.x) {
						show += "|" + STEP;
					} else {
						show += "|  " + whoOrWhat + "  ";
					}
				}
				show += "|\n";
			}
		}		
		return show;
	}
}
