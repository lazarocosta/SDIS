package protocols;

import udp.Server;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastBackup extends MulticastChannel {

    public MulticastBackup(int port, String address, int idSender, Server sender) {
        super(port, address, idSender, sender);
    }

    public String messagePutChunk(String version, int senderId, String fileId, int chunkNo, int replication, String body) {

        Message messageLine = new Message(version, senderId, fileId, chunkNo, replication);
        messageLine.setBody(body);
        String message = messageLine.msgPutChunk();

        return message;
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
