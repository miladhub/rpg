package rpg.tcp;

import rpg.game.Command;
import rpg.game.Direction;
import rpg.game.LookAround;
import rpg.game.Move;
import rpg.game.OutputPort;
import rpg.game.QuitGame;
import rpg.game.Say;
import rpg.game.EnterGame;
import rpg.game.TellPosition;
import rpg.game.TellWhatsNear;
import rpg.game.TellWhereabout;
import rpg.game.Travel;

public class Commands {
	private final OutputPort out;
	private String character;

	public Commands(OutputPort out) {
		this.out = out;
	}

	public Command createCommand(String command) throws UnknownCommandException {
		if (command.startsWith("enter as ")) {
			character = command.substring("enter as ".length());
			return new EnterGame(character, out);
		}
		if ("quit".equals(command)) {
			return new QuitGame(character);
		}
		if (command.startsWith("say ")) {
			return new Say(character, command.substring("say ".length()));
		}
		if (command.startsWith("go to ")) {
			return new Travel(character, command.substring("go to ".length()));
		}
		if (command.contains("where")) {
			return new TellWhereabout(character);
		}
		if (command.contains("near")) {
			return new TellWhatsNear(character);
		}
		if (command.contains("move")) {
			return new Move(character, parseDirection(command));
		}
		if (command.contains("position")) {
			return new TellPosition(character);
		}
		if (command.contains("look")) {
			return new LookAround(character);
		}
		throw new UnknownCommandException(command);
	}

	private Direction parseDirection(String command) {
		return Direction.valueOf(command.substring(command.indexOf("move") + "move".length()).trim());
	}
}
