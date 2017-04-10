package channels;

import protocol.Message;
import protocol.Restore;
import systems.Peer;

import java.io.*;
import java.net.*;

public class MulticastRestore extends MulticastChannel {

    public MulticastRestore(int port, String address, int senderId, ChannelGroup sender) {
        super(port, address, senderId, sender);
    }

    public void sendsMessageToSpecificPeer(byte[] packet, InetAddress peerIp, int peerPort) {
        try {
            DatagramPacket datagramPacketSend = new DatagramPacket(packet, packet.length, peerIp, peerPort);
            socket.send(datagramPacketSend);
            System.out.println("Sent message to specific Peer.");
        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    @Override
    public void run() {

        while (true) {
            try {
                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);

                byte[] data = new byte[datagramPacketReceive.getLength()];
                System.arraycopy(receive, 0, data, 0, data.length);

                Message msg = new Message();
                msg.separateFullMsg(data, datagramPacketReceive.getLength());

                System.out.println("Received packet length:" + datagramPacketReceive.getLength());
                System.out.println("Type receive: " + msg.getMsgType());

                switch (msg.getMsgType()) {
                    case "CHUNK": {
                        Restore.chunkHandler(msg);
                            Restore.chunkHandler(msg);
                            Peer.saveDatabase();
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