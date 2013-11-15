package rpg.tcp;

import java.io.IOException;
import java.io.PrintWriter;

public class WhatsNear implements Command {
	private final ClientContext context;

	public WhatsNear(ClientContext context) {
		this.context = context;
	}

	@Override
	public void execute(PrintWriter writer) throws IOException {
		context.session().whatsNear();
	}
}
