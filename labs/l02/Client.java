import javafx.scene.chart.PieChart;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

//client 228.5.6.7 4445 register wewe wewe
public class Client {

    private String multicastAddress;
    private int multicastPort;
    private String operation;
    private MulticastSocket multicastSocket;
    private MulticastSocket clientSocket;


    public void run(String[] args) throws UnknownHostException {

        this.multicastAddress = args[0];
        this.multicastPort = Integer.parseInt(args[1]);
        this.operation = args[2]; // "register" or "lookup"

       String port;
        InetAddress IpServer;

        String message;

        if (operation.equals("register")) {
            if (args.length == 4) {
                System.out.println("There is missing an argument for 'register' function.");
                return;
            }

            String plateNumber = args[3];
            String ownerName = args[4];
            message = "REGISTER " + plateNumber + " " + ownerName;

        } else if (operation.equals("lookup")) {
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
        InetAddress group = InetAddress.getByName(this.multicastAddress);


        System.out.println("Porta: " + this.multicastPort + " IP:" + group);

        try {
            clientSocket = new MulticastSocket(this.multicastPort);

            clientSocket.joinGroup(group);

            byte[] recive = new byte[100];
            DatagramPacket init = new DatagramPacket(recive, recive.length);
            System.out.println("aqui");
            clientSocket.receive(init);
            clientSocket.leaveGroup(group);


            port = new String(init.getData(),0,init.getLength());
            IpServer = init.getAddress();
            System.out.println("Ip:" + IpServer);
            System.out.println("porta:" + Integer.parseInt(port));

            DatagramSocket sendSocke = new DatagramSocket();
            DatagramPacket messageSend = new DatagramPacket(message.getBytes(), message.getBytes().length, IpServer,Integer.parseInt(port));


            sendSocke.send(messageSend);

            Thread.sleep(1000);

        } catch (IOException | InterruptedException A){

        }


    }

    public static void main(String[] args) throws UnknownHostException {

        try {
            Client myClient = new Client();
            myClient.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}