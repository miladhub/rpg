package rpg.game;

public class TellPosition implements Command {
	private String character;

	public TellPosition(String character) {
		this.character = character;
	}

	@Override
	public void execute(CommandContext commandContext) {
		LocalPosition pos = commandContext.characterLocations().localPosition(character);
		commandContext.outputPort(character).isAt(pos, commandContext.characterLocations().localMap(character));
	}

	@Override
	public String character() {
		return character;
	}
}
