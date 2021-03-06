package rpg.game;

public class TellPosition implements Command {
	@Override
	public void execute(String character, CommandContext commandContext) {
		LocalPosition pos = commandContext.characterLocations().localPosition(character);
		commandContext.character(character).isAt(pos, commandContext.characterLocations().localMap(character));
	}
}
