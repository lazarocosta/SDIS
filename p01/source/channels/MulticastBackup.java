package channels;

import protocol.Backup;
import protocol.Message;
import systems.Peer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

                Message msg = new Message();
                msg.separateFullMsg(messageComplete);
                System.out.println(msg.getMsgType());

                switch (msg.getMsgType()) {
                    case "PUTCHUNK": {
                        Backup.handleBackupRequest(msg);
                        Backup.saveChunk(msg.getVersion(),msg.getFileId(),msg.getChunkNo(), msg.getBody());
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
