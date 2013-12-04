package rpg.tcp;

import java.net.InetAddress;

public interface ServerOutputPort {
	void listening(int port);
	void cannotListen(String cause);
	void clientConnected(InetAddress inetAddress);
}
