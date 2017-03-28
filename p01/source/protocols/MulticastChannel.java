package protocols;

import udp.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Base class for a Multicast Channel.
 */
public class MulticastChannel implements Runnable {

    protected MulticastSocket socket;
    protected int port;
    protected InetAddress addr;
    protected int BUF_LENGTH = 65000;
    protected int senderId;
    protected Server sender;

    public MulticastChannel(int port, String address, int senderId, Server sender) {

        this.senderId = senderId;
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
            System.out.println("sends message");
        } catch (IOException A) {
            A.printStackTrace();
        }

    }

    public String messagePutChunk(String version, int senderId, String fileId, int chunkNo, int replication, String body) {

        Message messageLine = new Message(version, senderId, fileId, chunkNo, replication);
        messageLine.setBody(body);
        String message = messageLine.msgPutChunk();

        return message;
    }

    public String messageStored(String version, int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(version, idSender, fileId, ChunkNo);
        String message = messageLine.msgStored();

        System.out.println(" message Stored");
        return message;
    }

    public String messageDelete(String version, int idSender, String fileId) {

        Message messageLine = new Message(version, idSender, fileId);
        String message = messageLine.msgDelete();
        return message;
    }

    public String messageGetChunk(String version, int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(version, idSender, fileId, ChunkNo);
        String message = messageLine.msgGetChunk();

        System.out.println(" message GetChunk");
        return message;

    }

    public String messageChunk(String version, String fileId, int ChunkNo, String body) {

        Message messageLine = new Message(version, this.senderId, fileId, ChunkNo);
        messageLine.setBody(body);
        String message = messageLine.msgChunk();

        System.out.println(" message Chunk");
        return message;

    }

    public String messageRemoved(String version, int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(version, idSender, fileId, ChunkNo);
        String message = messageLine.msgRemoved();

        System.out.println(" message GetChunk");
        return message;

    }

    @Override
    public void run() {

    }

}
