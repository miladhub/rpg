package rpg.tcp;

import rpg.game.InputPort;
import rpg.game.OutputPort;

public interface ClientContext {
	void startSession(InputPort session);
	void endSession();
	InputPort inputPort();
	OutputPort outputPort();
}
