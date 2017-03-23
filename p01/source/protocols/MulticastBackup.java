package protocols;

import java.io.IOException;
import java.net.*;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastBackup implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;

    public MulticastBackup(int port, String address) {

        try {
            this.port = port;
            addr = InetAddress.getByName(address);
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(addr);

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sendsPutChunk(String version, int senderId, String fileId, int chunkNo, int replication, byte[] body) {
        try {
            Message messageLine = new Message(version, senderId, fileId, chunkNo, replication);
            String message = messageLine.msgPutChunk(body);

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

                byte[] receive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                Message message = new Message(messageComplete);

                switch (message.getMsgType()) {
                    case "PUTCHUNK": {
                        //faz o backup

                        //envia a resposta pelo Mcontrol
                    }
                    break;


                    default:
                        System.out.println("erro");
                }

            } catch (IOException A) {
                A.fillInStackTrace();
            }

        }
    }
}
