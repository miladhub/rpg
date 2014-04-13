package rpg.game;

public class QuitGame implements Command {
	@Override
	public void execute(String character, CommandContext commandContext) {
		CharacterHandle port = commandContext.character(character);
		port.heardFromGame("Bye.");
		port.endSession();
		commandContext.removeCharacter(character);
	}
}
