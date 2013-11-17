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
		for (String otherCharacter : context.charactersOthersThan(speaker)) {
			context.outputPort(otherCharacter).heardFrom(speaker, what);
		}
	}
}
