package rpg.game;

public class Move implements Command {
	private final String character;
	private final Direction dir;

	public Move(String character, Direction dir) {
		this.character = character;
		this.dir = dir;
	}

	@Override
	public void execute(CommandContext context) {
		context.characterLocations().move(character, dir);		
	}
}
