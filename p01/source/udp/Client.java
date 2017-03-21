package udp;

import java.io.*;
import java.net.*;

//client 228.5.6.7 4445 register wewe wewe

public class Client implements Runnable {

    private InetAddress multicastAddress = InetAddress.getByName("228.5.6.7");
    private DatagramPacket datagramPacketSend;
    private int multicastPortSend = 3000;
    private MulticastSocket multicastSocketSend;

    private DatagramPacket datagramPacketRecive;
    private int multicastPortRecive = 4000;
    private MulticastSocket multicastSocketRecive;

    private int BUF_LENGTH = 2000;


    @Override
    public void run() {

        this.recive();
    }

    public Client(String message) throws UnknownHostException {


        //Send Message
        try {
            multicastSocketRecive = new MulticastSocket(multicastPortRecive);
            multicastSocketRecive.joinGroup(multicastAddress);


            multicastSocketSend = new MulticastSocket(multicastPortSend);
            datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, multicastAddress, multicastPortSend);
            multicastSocketSend.send(datagramPacketSend);
            System.out.println("Send");

        } catch (IOException A) {
            A.printStackTrace();
        }


    }

    public void recive() {

        try {
            byte[] recive = new byte[BUF_LENGTH];
            datagramPacketRecive = new DatagramPacket(recive, recive.length);
            multicastSocketRecive.receive(datagramPacketRecive);
            System.out.println("Recevi a mensagem");
        } catch (IOException A) {
            A.printStackTrace();
        }

        String recive = new String(datagramPacketRecive.getData(), 0, datagramPacketRecive.getLength());

        System.out.println(recive);

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            Client client = new Client(args[0]);
            client.run();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

