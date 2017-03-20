import java.io.*;
import java.net.*;

//client 228.5.6.7 4445 register wewe wewe
public class Client2 {

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

    public static void main(String[] args) throws UnknownHostException {
        try {
            Client2 myClient = new Client2(args);
            myClient.intPorts();
            myClient.request();
            myClient.waitMessage();


        } catch (IOException A) {
            A.printStackTrace();
        }

    }

    public Client2(String[] args) throws UnknownHostException {

        if (args.length < 3) {
            System.out.println("There is missing an arguments");
            return;
        }

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

        System.out.println("Sended: " + message);


    }

    public void waitMessage() throws IOException {

        byte[] buf = new byte[1024];
        receivePacket = new DatagramPacket(buf, buf.length);
        sendSocke.receive(receivePacket);
        String result = new String(receivePacket.getData(), 0, receivePacket.getLength());

        System.out.println("Result: " + result);
    }
}