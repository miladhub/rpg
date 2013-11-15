package rpg.tcp;

import rpg.game.InputPort;

public interface ClientContext {
	void startSession(InputPort session);
	InputPort session();
}
