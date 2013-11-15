package rpg.tcp;

import java.io.IOException;
import java.io.PrintWriter;

public class WhereAmI implements Command {
	private final ClientContext context;

	public WhereAmI(ClientContext context) {
		this.context = context;
	}

	@Override
	public void execute(PrintWriter writer) throws IOException {
		context.session().whereabout();
	}
}
