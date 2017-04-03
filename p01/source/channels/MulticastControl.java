package channels;

import protocol.Backup;
import protocol.Delete;
import protocol.Message;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastControl extends MulticastChannel {

    public MulticastControl(int port, String address, int senderId, ChannelGroup sender) {
        super(port, address, senderId, sender);
    }

    public byte[] getChunkOfSender(String version, String fileId, int chunkNo) throws IOException {

        String pathSenderId = "Sender" + senderId;
        String pathChunkNo = pathSenderId + "/" + fileId + "/" + chunkNo + ".txt"; // TEMOS QUE VER AQUI A TERMINAÇAO

        byte[] body;

        File f = new File(pathChunkNo);

        if (f.exists()) {

            System.out.println("existe chunk");
            InputStream is = new FileInputStream(pathChunkNo);
            int size = is.available();


            body = new byte[size];

            int re = is.read(body, 0, size);

            System.out.println(body.toString());
        } else return null;

        return body;
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
                msg.separateMsg(messageComplete);

                System.out.println("Type " + msg.getMsgType());
                if (msg.getSenderId() != this.senderId) {
                    switch (msg.getMsgType()) {
                        case "GETCHUNK": {

                            byte[] body = this.getChunkOfSender(msg.getVersion(), msg.getFileId(), msg.getChunkNo());
                            System.out.println(body);

                            if (body != null) {
                                String sendToServer = this.messageChunk(msg.getVersion(), msg.getFileId(), msg.getChunkNo(), body);
                                this.sender.sendForRestore(sendToServer);
                            }
                        }
                        break;
                        case "DELETE": {
                            Delete.deleteFile(msg.getFileId());
                            //this.deleteFile(msg.getFileId());
                            System.out.println("Delete");
                        }
                        break;
                        case "REMOVED": {
                            //
                        }
                        break;
                        case "STORED": {
                            if (msg.getSenderId() != senderId) {
                                Backup.storedHandler(msg);
                                System.out.println("STORED");
                            }
                            //
                        }
                        break;
                        default:
                            System.out.println("discard");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
