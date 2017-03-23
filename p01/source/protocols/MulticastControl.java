package protocols;

import java.io.IOException;
import java.net.*;

/**
 * Created by Lazaro on 23/03/2017.
 */
public class MulticastControl implements Runnable {

    private MulticastSocket socket;
    private int port;
    private InetAddress addr;
    private int BUF_LENGTH = 65000;

    public MulticastControl(int port, String address) {

        try {
            this.port = port;
            addr = InetAddress.getByName(address);
            this.socket = new MulticastSocket(port);
            this.socket.joinGroup(addr);

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public void sends(String message) {
        try {
            //Message messageLine = new Message("vers", 1, "file", 1, 2);
            //String message = messageLine.generatePutChunkLine();

            DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, port);
            socket.send(datagramPacketSend);
            System.out.println("sends message");

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("MulticastControl");

        while (true) {
            try {

                byte[] recive = new byte[BUF_LENGTH];
                DatagramPacket datagramPacketReceive = new DatagramPacket(recive, recive.length);
                socket.receive(datagramPacketReceive);
                String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

                Message message = new Message(messageComplete);

                switch (message.getMsgType()) {
                    case "GETCHUNK": {
                        //faz o resture
                        //envia a resposta pelor resure
                    }
                    break;
                    case "DELETE": {
                        Delete deleteFile = new Delete(message.getFileId());
                        //faz o delete
                    }
                    break;
                    case "REMOVED": {
                        //
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
