package rpg.game;

public interface Command {
	void execute(String character, CommandContext commandContext);
}
