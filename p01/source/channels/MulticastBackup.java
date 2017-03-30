package channels;

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

    public void backupFile(String version, String fileId, int chunkNo, byte[] body) {

        String pathSenderId = "Sender" + senderId;
        String pathFileId = pathSenderId + "/" + fileId;
        String pathChunkNo = pathFileId + "/" + chunkNo + ".txt";

        File f = new File(pathFileId);

        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            f.mkdirs();
            System.out.println("criou path ");
        }

        try {

            OutputStream is = new FileOutputStream(fChunk);
            System.out.println("criou path ");

            System.out.println("length" + body.length);
            is.write(body);

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        if (msg.getSenderId() != senderId) {
                            this.backupFile(msg.getVersion(), msg.getFileId(), msg.getChunkNo(), msg.getBody());

                            String sendToServer = this.messageStored(senderId, msg.getFileId(), msg.getChunkNo());
                            //envia a resposta pelo Mcontrol
                            this.sender.sendForControl(sendToServer);
                        }
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
