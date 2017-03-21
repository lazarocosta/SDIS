package systems;

import java.io.IOException;

/**
 *
 */
public class Peer {

    udp.Client udpClient = new udp.Client("230.0.0.0", 3000, "Hello! I am Lindsay Lohan.");
    udp.Server udpServer = new udp.Server(2000, "230.0.0.0", 3000);

    public Peer() throws IOException, InterruptedException {
    }
}
