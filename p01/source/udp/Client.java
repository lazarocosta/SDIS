package udp;

import java.io.*;
import java.net.*;

//client 228.5.6.7 4445 register wewe wewe
public class Client implements Runnable{

    private String multicastAddress;
    private int multicastPort;
    private String operation;
    private MulticastSocket clientSocket;
    private InetAddress group;
    private DatagramPacket initialization;
    private DatagramPacket receivePacket;
    private int portServer;
    private InetAddress IpServer;
    private String message;
    private DatagramSocket sendSocke;
    private String port;

    @Override
    public void run() {
        try {

            this.intPorts();
            this.request();
            this.waitMessage();

        } catch (IOException A) {
            A.printStackTrace();
        }
    }

    public Client(String multicastAddress, Integer multicastPort, String operation) throws UnknownHostException {


        this.multicastAddress = multicastAddress;
        this.multicastPort = multicastPort;
        this.operation = operation;

        message = operation;

        // Get the address that we are going to connect to.
        group = InetAddress.getByName(this.multicastAddress);
    }

    public void intPorts() throws IOException {

        clientSocket = new MulticastSocket(this.multicastPort);
        clientSocket.joinGroup(group);

        byte[] recive = new byte[100];
        initialization = new DatagramPacket(recive, recive.length);
        clientSocket.receive(initialization);
        this.port = new String(initialization.getData(), 0, initialization.getLength());
        clientSocket.leaveGroup(group);

        portServer = initialization.getPort();
        IpServer = initialization.getAddress();
    }

    public void request() throws IOException {

        sendSocke = new DatagramSocket();
        System.out.println(this.port);
        DatagramPacket messageSend = new DatagramPacket(message.getBytes(), message.getBytes().length, IpServer, 3000);
        sendSocke.send(messageSend);

        System.out.println("Sent: " + message);


    }

    public void waitMessage() throws IOException {

        byte[] buf = new byte[1024];
        receivePacket = new DatagramPacket(buf, buf.length);
        sendSocke.receive(receivePacket);
        String result = new String(receivePacket.getData(), 0, receivePacket.getLength());

        System.out.println("Result: " + result);
    }
}