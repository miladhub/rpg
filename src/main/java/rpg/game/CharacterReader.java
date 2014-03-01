package rpg.game;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CharacterReader {
	private final CharacterLocations charLocations;

	public CharacterReader(WorldMap map) {
		charLocations = new CharacterLocations(map);
	}

	public CharacterLocations readFromString(String charLocsAsString) {
		String[] charLines = charLocsAsString.split("\n");
		for (String charLine : charLines) {
			readCharacterLocation(charLine);
		}
		return charLocations;
	}

	private void readCharacterLocation(String charLine) {
		String[] tokens = charLine.split("\t");
		String character = tokens[0];
		charLocations.setCharacterAtLocation(character, tokens[1], tokens[2]);
		readLocalPosition(character, tokens[3]);
	}

	private void readLocalPosition(String character, String localPosAsString) {
		String[] xyTokens = localPosAsString.split(",");
		int x = Integer.parseInt(xyTokens[0].trim());
		int y = Integer.parseInt(xyTokens[1].trim());
		charLocations.setLocalPosition(character, x, y);
	}

	public CharacterLocations readFromFile(File file) throws IOException {
		return readFromString(FileUtils.readFileToString(file));
	}
}
