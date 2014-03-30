package rpg.game;

public class Move implements Command {
	private final Direction dir;

	public Move(Direction dir) {
		this.dir = dir;
	}

	@Override
	public void execute(String character, CommandContext context) {
		context.characterLocations().move(character, dir);		
	}
}
