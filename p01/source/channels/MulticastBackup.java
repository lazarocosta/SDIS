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
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());
               // System.out.println("_------------" + new String(messageComplete));
                Message msg = new Message();
                msg.separateFullMsg(messageComplete);
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
