package rpg.game;

public class Say implements Command {
	private final String what;

	public Say(String what) {
		this.what = what;
	}
	
	@Override
	public void execute(String character, CommandContext context) {
		for (String otherCharacter : context.nearbyCharacters(character)) {
			context.outputPort(otherCharacter).heardFrom(character, what);
		}
	}
}
