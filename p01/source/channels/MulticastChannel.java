package channels;

import chunk.Chunk;
import protocol.Message;

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
    protected ChannelGroup sender;

    public MulticastChannel(int port, String address, int senderId, ChannelGroup sender) {

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

    public void sleep(int time_in_ms){
        try {
            Thread.sleep(time_in_ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendsMessage(byte[] packet) {

        try {
            DatagramPacket datagramPacketSend = new DatagramPacket(packet, packet.length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message");
        } catch (IOException A) {
            A.printStackTrace();
        }

    }

    public byte[] messagePutChunk(int senderId, Chunk c) {

        Message messageLine = new Message(senderId, c.getFileId(), c.getChunkNo(), c.getReplicationDegree());
        messageLine.setBody(c.getData());
        byte[] message = messageLine.msgPutChunk();

        return message;
    }

    public byte[] messageStored(int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(idSender, fileId, ChunkNo);
        byte[] message = messageLine.msgStored();

        System.out.println(" message Stored");
        return message;
    }

    public byte[] messageDelete(int idSender, String fileId) {

        Message messageLine = new Message(idSender, fileId);
        byte[] message = messageLine.msgDelete();

        return message;
    }

    public byte[] messageGetChunk(int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(idSender, fileId, ChunkNo);
        byte[] message = messageLine.msgGetChunk();

        System.out.println(" message GetChunk");
        return message;

    }

    public byte[] messageChunk(String fileId, int ChunkNo, byte[] body) {

        Message messageLine = new Message(this.senderId, fileId, ChunkNo);
        messageLine.setBody(body);
        byte[] message = messageLine.msgChunk();

        System.out.println(" message Chunk");
        return message;

    }

    public byte[] messageRemoved(int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(idSender, fileId, ChunkNo);
        byte[] message = messageLine.msgRemoved();

        System.out.println(" message GetChunk");
        return message;

    }

    @Override
    public void run() {

    }

}
