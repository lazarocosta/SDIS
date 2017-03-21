package udp;

import protocols.Message;

import java.io.IOException;

import java.net.*;

// run server 3000 228.5.6.7 4445

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class Server implements Runnable {

    private InetAddress multicastAddress = InetAddress.getByName("228.5.6.7");
    private DatagramPacket datagramPacketSend;
    private int multicastPortSend = 4000;
    private MulticastSocket multicastSocketSend;

    private DatagramPacket datagramPacketRecive;
    private int multicastPortRecive = 3000;
    private MulticastSocket multicastSocketRecive;

    private int BUF_LENGTH = 2000;

    @Override
    public void run() {

    }

    public Server() throws UnknownHostException, InterruptedException, IOException {


        try {
            System.out.println("espero");
            multicastSocketSend= new MulticastSocket(multicastPortSend);

            multicastSocketRecive = new MulticastSocket(multicastPortRecive);
            multicastSocketRecive.joinGroup(multicastAddress);

            byte[] recive = new byte[BUF_LENGTH];
            datagramPacketRecive = new DatagramPacket(recive, recive.length);
            multicastSocketRecive.receive(datagramPacketRecive);
            System.out.println("recive");

            this.send();
        } catch (IOException A) {
            A.printStackTrace();
        }


    }

    public void send() throws IOException {


        Message messageLine = new Message();
        String message = messageLine.generatePutChunkLine("1",1,"fileid",1,1);

        DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, multicastAddress, multicastPortSend);
        multicastSocketSend.send(datagramPacketSend);
        System.out.println("enviou");

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            Server server = new Server();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

