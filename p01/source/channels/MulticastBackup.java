package channels;

import protocol.Backup;
import protocol.Message;

import java.io.IOException;
import java.net.*;


/**
 *
 */
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
                msg.separateFullMsg(datagramPacketReceive.getData());

                System.out.println(msg.getMsgType());

                switch (msg.getMsgType()) {
                    case "PUTCHUNK": {
                        Backup.backupHandler(msg);
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
