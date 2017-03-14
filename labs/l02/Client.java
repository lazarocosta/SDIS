import javafx.scene.chart.PieChart;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

//client 228.5.6.7 4445 register wewe wewe
public class Client {

    private String multicastAddress;
    private int multicastPort;
    private String operation;
    private MulticastSocket clientSocket;
    private InetAddress group;
    private DatagramPacket initialization;
    private DatagramPacket recivePacket;
    private int portServer;
    private InetAddress IpServer;
    private String message;
    private DatagramSocket sendSocke;

    public static void main(String[] args) throws UnknownHostException {
        try {
            Client myClient = new Client(args);
            myClient.intPorts();
            myClient.request();
            myClient.waitMessage();


        } catch (IOException A) {

        }

    }

    public Client(String[] args) throws UnknownHostException {

        this.multicastAddress = args[0];
        this.multicastPort = Integer.parseInt(args[1]);
        this.operation = args[2]; // "register" or "lookup"


        //valid message
        if (operation.equals("register") | operation.equals("REGISTER")) {
            if (args.length == 4) {
                System.out.println("There is missing an argument for 'register' function.");
                return;
            }

            String plateNumber = args[3];
            String ownerName = args[4];
            message = "REGISTER " + plateNumber + " " + ownerName;

        } else if (operation.equals("lookup") | operation.equals("LOOKUP")) {
            if (args.length == 5) {
                System.out.println("There is an extra argument for 'lookup' function.");
                return;
            }

            String plateNumber = args[3];
            message = "LOOKUP " + plateNumber;

        } else {
            System.out.println("Bad argument for 'operation.'");
            return;
        }


        // Get the address that we are going to connect to.
        group = InetAddress.getByName(this.multicastAddress);

    }

    public void intPorts() throws IOException {

        clientSocket = new MulticastSocket(this.multicastPort);
        clientSocket.joinGroup(group);

        byte[] recive = new byte[100];
        initialization = new DatagramPacket(recive, recive.length);
        clientSocket.receive(initialization);
        System.out.println("recebeu");
        clientSocket.leaveGroup(group);

        portServer = initialization.getPort();
        IpServer = initialization.getAddress();
        System.out.println("Ip:" + IpServer);
        System.out.println("porta:" + portServer);
    }

    public void request() throws IOException {

        sendSocke = new DatagramSocket();
        DatagramPacket messageSend = new DatagramPacket(message.getBytes(), message.getBytes().length, IpServer, portServer);
        sendSocke.send(messageSend);


    }

    public void waitMessage() throws IOException {

        System.out.print("espera");
        byte[] buf = new byte[1024];
        recivePacket = new DatagramPacket(buf, buf.length);
        sendSocke.receive(recivePacket);

    }


}