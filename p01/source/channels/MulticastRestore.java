package channels;

import protocol.Message;

import java.io.*;
import java.net.*;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastRestore extends MulticastChannel {

    public MulticastRestore(int port, String address, int senderId, ChannelGroup sender) {
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


                System.out.println(messageComplete);
                Message msg = new Message();
                msg.separateFullMsg(messageComplete);

                System.out.println(msg.getMsgType());
                switch (msg.getMsgType()) {
                    case "CHUNK": {
                        System.out.println(msg.getBody());
                       // Restore.restoreFile(msg);
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