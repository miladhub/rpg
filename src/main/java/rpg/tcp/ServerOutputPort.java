package rpg.tcp;

import java.net.InetAddress;

public interface ServerOutputPort {
	void listening(int port) throws Exception;
	void clientConnected(InetAddress inetAddress);
}
