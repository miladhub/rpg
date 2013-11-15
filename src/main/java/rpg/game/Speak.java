package rpg.game;

public class Speak {
	private final String speaker;
	private final String what;

	public Speak(String speaker, String what) {
		this.speaker = speaker;
		this.what = what;
	}
	
	public void execute(CommandContext context) {
		for (String otherCharacter : context.charactersOthersThan(speaker)) {
			context.outputPort(otherCharacter).heardFrom(speaker, what);
		}
	}
}
