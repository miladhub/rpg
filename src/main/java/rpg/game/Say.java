package rpg.game;

public class Say implements Command {
	private final String speaker;
	private final String what;

	public Say(String speaker, String what) {
		this.speaker = speaker;
		this.what = what;
	}
	
	@Override
	public void execute(CommandContext context) {
		for (String otherCharacter : context.nearbyCharacters(speaker)) {
			context.outputPort(otherCharacter).heardFrom(speaker, what);
		}
	}

	@Override
	public String character() {
		return speaker;
	}
}
