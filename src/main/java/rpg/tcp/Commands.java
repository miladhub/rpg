package rpg.tcp;

import rpg.game.Command;
import rpg.game.CommandContext;
import rpg.game.Direction;
import rpg.game.Fight;
import rpg.game.LookAround;
import rpg.game.Move;
import rpg.game.CharacterHandle;
import rpg.game.QuitGame;
import rpg.game.Say;
import rpg.game.EnterGame;
import rpg.game.Scripts;
import rpg.game.Sleep;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.TellWhereabout;
import rpg.game.Travel;

public class Commands {
	private final CharacterHandle handle;
	private String character;

	public Commands(CharacterHandle handle) {
		this.handle = handle;
	}

	public CharacterCommand createCharacterCommand(String command) throws UnknownCommandException {
		return new CharacterCommand(parseCommandAndCharacter(command), character);
	}

	private Command parseCommandAndCharacter(String command) throws UnknownCommandException {
		if (command.startsWith("enter")) {
			character = command.substring("enter as ".length());
			return new EnterGame(handle);
		}
		if ("quit".equals(command)) {
			return new QuitGame();
		}
		if (command.startsWith("say")) {
			return new Say(command.substring("say ".length()));
		}
		if (command.startsWith("go")) {
			return new Travel(command.substring("go to ".length()));
		}
		if (command.contains("where")) {
			return new TellWhereabout();
		}
		if (command.contains("near")) {
			return new TellWhatsNear();
		}
		if (command.contains("move")) {
			return new Move(Direction.valueOf(command.substring(command.indexOf("move") + "move".length()).trim()));
		}
		if (command.contains("position")) {
			return new TellPosition();
		}
		if (command.contains("look")) {
			return new LookAround();
		}
		if (command.contains("sleep")) {
			return new Command() {
				@Override
				public void execute(String character, CommandContext commandContext) {
					commandContext.addScript(Scripts.aScript(new Sleep(character)).lasting(5));
				}
			};
		}
		if (command.contains("attack")) {
			final String opponent = command.split(" ")[1];
			handle.heardFromGame("You are attacking " + opponent);
			return new Command() {
				@Override
				public void execute(String character, CommandContext commandContext) {
					commandContext.addScript(Scripts.aScript(new Fight(opponent)).lasting(3));
				}
			};
		}
		throw new UnknownCommandException(command);
	}
}
