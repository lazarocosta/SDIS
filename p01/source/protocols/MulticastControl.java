package protocols;

import java.io.IOException;
import java.net.*;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastControl implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;

    public MulticastControl(int port, String address) {

        try {
            this.port = port;
            addr = InetAddress.getByName(address);
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(addr);

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsDelete(String version, int serverId, String fileId) {
        try {
            Message messageLine = new Message(version, serverId, fileId);
            String message = messageLine.msgDelete();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Delete");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsStored(String version, int serverId, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, serverId, fileId, ChunkNo);
            String message = messageLine.msgStored();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Stored");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsGetChunk(String version, int serverId, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, serverId, fileId, ChunkNo);
            String message = messageLine.msgGetChunk();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message GetChunk");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsRemoved(String version, int serverId, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, serverId, fileId, ChunkNo);
            String message = messageLine.msgRemoved();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message GetChunk");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("MulticastControl");

        while (true) {
            try {

                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                Message msg = new Message();
                msg.separateMsg(messageComplete);


                switch (msg.getMsgType()) {
                    case "GETCHUNK": {
                        //
                    }
                    break;
                    case "DELETE": {
                        Delete deleteFile = new Delete(msg.getFileId());
                        //faz o delete
                    }
                    break;
                    case "REMOVED": {
                        //
                    }
                    break;
                    case "STORED": {
                        //
                    }
                    break;
                    default:
                        System.out.println("discard");
                }


            } catch (IOException A) {
                A.fillInStackTrace();
            }
        }
    }
}
