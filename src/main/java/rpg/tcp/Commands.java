package rpg.tcp;

import rpg.game.Command;
import rpg.game.Direction;
import rpg.game.Move;
import rpg.game.OutputPort;
import rpg.game.QuitGame;
import rpg.game.Say;
import rpg.game.StartGame;
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
			return new StartGame(character, out);
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
		if ("where am I".equalsIgnoreCase(command)) {
			return new TellWhereabout(character);
		}
		if ("what's near".equalsIgnoreCase(command)) {
			return new TellWhatsNear(character);
		}
		if (command.contains("move")) {
			return new Move(character, parseDirection(command));
		}
		if (command.contains("position")) {
			return new TellPosition(character);
		}
		throw new UnknownCommandException(command);
	}

	private Direction parseDirection(String command) {
		return Direction.valueOf(command.substring(command.indexOf("move") + "move".length()).trim());
	}
}
