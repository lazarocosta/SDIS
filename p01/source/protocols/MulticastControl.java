package protocols;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastControl implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;
    private int idSender;

    public MulticastControl(int port, String address, int idSender) {

        this.idSender = idSender;
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
            Message messageLine = new Message(version, idSender, fileId);
            String message = messageLine.msgDelete();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Delete");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsStored(String version, int idSender, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, idSender, fileId, ChunkNo);
            String message = messageLine.msgStored();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Stored");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsGetChunk(String version, int idSender, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, idSender, fileId, ChunkNo);
            String message = messageLine.msgGetChunk();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message GetChunk");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsRemoved(String version, int idSender, String fileId, int ChunkNo) {
        try {
            Message messageLine = new Message(version, idSender, fileId, ChunkNo);
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
                System.out.println("Control wait");
                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());
                Message msg = new Message();
                msg.separateMsg(messageComplete);

                System.out.println(msg.getMsgType());
                if (msg.getSenderId() != idSender) {
                    switch (msg.getMsgType()) {
                        case "GETCHUNK": {
                            System.out.println("receive Get");
                            System.out.println("senderId" + msg.getSenderId());
                            System.out.println("chunkNo" + msg.getChunkNo());
                            String body = this.getChunkOfSender(msg.getVersion(), msg.getSenderId(), msg.getFileId(), msg.getChunkNo());
                            System.out.println(body);
                            //ENVIAR RESULT PARA O RESTORE
                            //
                        }
                        break;
                        case "DELETE": {
                            this.deleteFile(msg.getSenderId(), msg.getFileId());
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

    private void deleteFile(int senderId, String fileId) {

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

    public String getChunkOfSender(String version, int senderId, String fileId, int chunkNo) throws IOException {

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
