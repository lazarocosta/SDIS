package systems;

import protocols.MulticastBackup;
import protocols.MulticastControl;
import protocols.MulticastRestore;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.AlreadyBoundException;

/**
 *
 */
public class Peer {

   private static PeerInfo info; // info that defines the peer
    private static MulticastSocket mcSocket;    // socket through which the Peer will provide service

    private static rmi.ServerInitiation rmiServer;
    private static udp.Server udpServer;

    // Main method for running a peer
    // args = "1.0", 2, "accessPoint", "228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000
    public static void main(String[] args) throws IOException, AlreadyBoundException, InterruptedException {

        rmiServer = new rmi.ServerInitiation(args[0], Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6]), args[7], Integer.parseInt(args[8]),"backup");
        udpServer = new udp.Server(args[0], Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6]), args[7], Integer.parseInt(args[8]));

    }



}
