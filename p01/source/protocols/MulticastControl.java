package protocols;

import udp.Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastControl extends MulticastChannel {

    public MulticastControl(int port, String address, int senderId, Server sender) {
        super(port, address, senderId, sender);
    }

    public String messageDelete(String version, int idSender, String fileId) {

        Message messageLine = new Message(version, idSender, fileId);
        String message = messageLine.msgDelete();
        return message;


    }

    public String messageStored(String version, int idSender, String fileId, int ChunkNo) {

        Message messageLine = new Message(version, idSender, fileId, ChunkNo);
        String message = messageLine.msgStored();

        System.out.println(" message Stored");
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
        System.out.println("MulticastControl");

        while (true) {
            try {

                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());
                Message msg = new Message();
                msg.separateMsg(messageComplete);

                System.out.println("Type" + msg.getMsgType());
                if (msg.getSenderId() != this.senderId) {
                    switch (msg.getMsgType()) {
                        case "GETCHUNK": {

                            String body = this.getChunkOfSender(msg.getVersion(), msg.getFileId(), msg.getChunkNo());
                            System.out.println(body);

                            if (body != null) {
                                String sendToServer = this.messageChunk(msg.getVersion(), msg.getFileId(), msg.getChunkNo(), body);
                                this.sender.sendForRestore(sendToServer);
                            }
                        }
                        break;
                        case "DELETE": {
                            this.deleteFile(msg.getFileId());
                            System.out.println("Delete");
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
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFile(String fileId) {

        String pathSenderId = "Sender" + senderId;
        String pathFileId = pathSenderId + "/" + fileId;

        File f = new File(pathFileId);
        File fileSender = new File(pathSenderId);

        if (f.exists()) {
            for (File file : f.listFiles()) {
                file.delete();
                System.out.println("delete file into" + pathFileId);
            }

            for (File file : fileSender.listFiles()) {
                if (file.compareTo(f) == 0) {//equals
                    file.delete();
                    System.out.println("delete diretory" + pathFileId);
                }
            }
        }
    }

    public String getChunkOfSender(String version, String fileId, int chunkNo) throws IOException {

        String pathSenderId = "Sender" + senderId;
        String pathChunkNo = pathSenderId + "/" + fileId + "/" + chunkNo + ".txt"; // TEMOS QUE VER AQUI A TERMINAÇAO

        String body = "";

        File f = new File(pathChunkNo);

        if (f.exists()) {

            System.out.println("existe chunk");
            InputStream is = new FileInputStream(pathChunkNo);
            int size = is.available();

            for (int i = 0; i < size; i++) {
                int re = is.read();
                String read = Character.toString((char) re);
                body += read;
            }
        } else return null;

        return body;
    }
}
