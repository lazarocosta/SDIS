package channels;

import protocol.Backup;
import protocol.Delete;
import protocol.Message;
import protocol.Restore;
import systems.Peer;

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


                // System.out.println("_------------" + new String(messageComplete));

                System.out.println("Type " + msg.getMsgType());
                switch (msg.getMsgType()) {
                    case "GETCHUNK": {
                        if(Peer.enhancements == true)
                            Restore.getChunkHandler(msg);
                        else
                            Restore.getChunkHandler(msg, datagramPacketReceive.getAddress(), datagramPacketReceive.getPort());

                        Peer.saveDatabase();
                    }
                    break;
                    case "DELETE": {
                        Delete.deleteFile(msg);
                        Peer.saveDatabase();
                    }
                    break;
                    case "REMOVED": {
                        //
                    }
                    break;
                    case "STORED": {
                        if (msg.getSenderId() != senderId) {
                            Backup.storedHandler(msg);
                            Peer.saveDatabase();
                        }
                        //
                    }
                    break;
                    default:
                        System.out.println("discard");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
