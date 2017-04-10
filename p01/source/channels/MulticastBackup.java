package channels;

import protocol.Backup;
import protocol.Message;
import systems.Peer;

import java.io.IOException;
import java.net.*;

public class MulticastBackup extends MulticastChannel {

    public MulticastBackup(int port, String address, int senderId, ChannelGroup sender) {
        super(port, address, senderId, sender);
    }

    @Override
    public void run() {

        while (true) {
            try {
                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);

                Message msg = new Message();
                msg.separateFullMsg(datagramPacketReceive.getData(), datagramPacketReceive.getLength());

                System.out.println("HEREEE datagram length: " + datagramPacketReceive.getLength());
                System.out.println("HEREEE msg body length: " + msg.getBody().length);


                System.out.println(msg.getMsgType());

                switch (msg.getMsgType()) {
                    case "PUTCHUNK": {
                        Backup.backupHandler(msg);
                        Peer.saveDatabase();
                        System.out.println("Saved chunk.");
                    }
                    break;
                    default:
                        System.out.println("Discard.");
                }

            } catch (IOException A) {
                A.fillInStackTrace();
            }
        }
    }
}
