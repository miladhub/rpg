package rpg.tcp;

import java.io.PrintWriter;

public class Say implements Command {
	private final ClientContext context;
	private final String command;

	public Say(ClientContext context, String command) {
		this.context = context;
		this.command = command;
	}

	@Override
	public void execute(PrintWriter writer) {
		context.session().say(command.substring("say ".length()));
	}
}
