package rpg.tcp;

import rpg.game.Command;

public class CharacterCommand {
	public final Command command;
	public final String character;

	public CharacterCommand(Command command, String character) {
		this.command = command;
		this.character = character;
	}
}
