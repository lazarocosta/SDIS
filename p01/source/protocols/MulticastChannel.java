package protocols;

import udp.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Base class for a Multicast Channel.
 */
public class MulticastChannel implements Runnable{

    protected MulticastSocket socket;
    protected int port;
    protected InetAddress addr;
    protected int BUF_LENGTH = 65000;
    protected int idSender;
    protected Server sender;

    public MulticastChannel(int port, String address, int idSender, Server sender) {

        this.idSender = idSender;
        this.sender = sender;
        try {
            this.port = port;
            addr = InetAddress.getByName(address);
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(addr);

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsMessage(String message) {

        try {
            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Chunk");
        } catch (IOException A) {
            A.printStackTrace();
        }

    }


    @Override
    public void run() {

    }


}
