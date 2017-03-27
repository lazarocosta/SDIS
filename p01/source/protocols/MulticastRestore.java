package protocols;

import udp.Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastRestore implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;
    private int idSender;
    private Server sender;

    public MulticastRestore(int port, String address, int idSender, Server sender) {

        this.idSender = idSender;
        this.sender= sender;
        try {
            this.port = port;
            addr = InetAddress.getByName(address);
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(addr);

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public String messageChunk(String version, int senderId, String fileId, int chunkNo, String body) {

        Message messageLine = new Message(version, senderId, fileId, chunkNo);
        messageLine.setBody(body);
        String message = messageLine.msgPutChunk();

        System.out.println("sends message Chunk");
        return message;

    }

    public void sendsMessage(String message) {

        try {
            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message Chunk");
        } catch (IOException A) {
            A.printStackTrace();
        }

    }

    @Override
    public void run() {
        System.out.println("MulticastRestore");

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
                    case "CHUNK": {
                        if (idSender != msg.getSenderId()) {
                            System.out.println(idSender);
                            this.restoreFile(msg);
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

    public void restoreFile(Message message) {

        String pathSenderId = "Sender" + idSender;
        String pathFileId = pathSenderId + "/" + message.getFileId();
        String pathChunkNo = pathFileId + "/" + message.getChunkNo() + "copia.txt";

        File f = new File(pathFileId);
        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            f.mkdir();
            System.out.println("fez path");
        }

        try {
            OutputStream is = new FileOutputStream(fChunk);
            String body = message.getBody();

            for (int i = 0; i < body.length(); i++) {
                is.write(body.charAt(i));
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}