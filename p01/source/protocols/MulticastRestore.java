package protocols;

import udp.Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastRestore extends MulticastChannel {

    public MulticastRestore(int port, String address, int senderId, Server sender) {
        super(port, address, senderId, sender);
    }

    public void restoreFile(Message message) {

        String pathSenderId = "Sender" + senderId;
        String pathFileId = pathSenderId + "/" + message.getFileId();
        String pathChunkNo = pathFileId + "/" + message.getChunkNo() + "copia.txt";

        File f = new File(pathFileId);
        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            //  f.mkdir();
            System.out.println("nao fez restore");
        } else {

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
                    case "CHUNK": {

                        System.out.println(senderId);
                        this.restoreFile(msg);
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