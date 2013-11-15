package rpg.tcp;

import java.io.IOException;
import java.io.PrintWriter;

public class GoTo implements Command {
	private final ClientContext context;
	private final String command;

	public GoTo(ClientContext context, String command) {
		this.context = context;
		this.command = command;
	}

	@Override
	public void execute(PrintWriter writer) throws IOException {
		context.session().moveTo(command.substring("go to ".length()));
	}
}
