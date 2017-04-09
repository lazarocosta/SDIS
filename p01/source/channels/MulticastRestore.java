package channels;

import protocol.Message;
import protocol.Restore;

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

                Message msg = new Message();
                msg.separateFullMsg(datagramPacketReceive.getData());

                System.out.println("Received getData:" + new String(datagramPacketReceive.getData()));

                System.out.println("Type receive: "+ msg.getMsgType());
                switch (msg.getMsgType()) {
                    case "CHUNK": {
                            Restore.chunkHandler(msg);
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