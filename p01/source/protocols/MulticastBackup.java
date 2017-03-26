package protocols;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastBackup implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;
    private int idSender;

    public MulticastBackup(int port, String address, int idSender) {

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

    public String messagePutChunk(String version, int senderId, String fileId, int chunkNo, int replication, String body) {

        Message messageLine = new Message(version, senderId, fileId, chunkNo, replication);
        messageLine.setBody(body);
        String message = messageLine.msgPutChunk();

        return message;
    }

    public void sendsMessage(String message) {


        try {
            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message PutChunk");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("MulticastBackup");

        while (true) {
            try {
                System.out.println("backup wait");
                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                Message msg = new Message();
                msg.separateFullMsg(messageComplete);

                switch (msg.getMsgType()) {
                    case "PUTCHUNK": {
                        //faz o backup

                        //envia a resposta pelo Mcontrol
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
