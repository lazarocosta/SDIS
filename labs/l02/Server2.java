import java.io.IOException;
import java.net.*;
import java.util.HashMap;

// run server 3000 228.5.6.7 4445
public class Server2 {

    private int servicePort;
    private String multicastAddress;
    private int multicastPort;
    private MulticastSocket multicastSocket;
    private InetAddress addr;
    private HashMap<String, String> dataBase;
    private DatagramSocket serverSocket;

    public  Server2(String[] args) throws UnknownHostException, InterruptedException, IOException {

        if (args.length != 3) {
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>.");
            return;
        }

        this.servicePort = Integer.parseInt(args[0]);
        this.multicastAddress = args[1];
        this.multicastPort = Integer.parseInt(args[2]);

        multicastSocket = new MulticastSocket();
        dataBase= new HashMap<String, String>();

        // Get the address that we are going to connect to.
        addr = InetAddress.getByName(multicastAddress);

        MulticastThread multicastThread = new MulticastThread();
        multicastThread.start();

        // Open a new DatagramSocket, which will be used to send the data.
        serverSocket = new DatagramSocket(this.servicePort);

        while (true) {
            byte[] msg = new byte[2000];
            System.out.println("espero");

            //receiver packet
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length);
            serverSocket.receive(msgPacket);
            System.out.println("Server receiver packet with msg: " + msg);

            String buffer = new String(msgPacket.getData(), 0, msgPacket.getLength());
            String[] recive = buffer.split("\\s");
            int port = msgPacket.getPort();
            InetAddress adress = msgPacket.getAddress();


            System.out.println(recive[0]);


            if (recive[0].equals("REGISTER")) {
                if (recive.length != 3) {
                    System.out.println("Invalid number Commands");
                    return;
                }

                 String resultMessage = this.registe(recive[1], recive[2]);
               send(resultMessage, port, adress);


            } else if (recive[0].equals("LOOKUP")) {
                if (recive.length != 2) {
                    System.out.println("Invalid number Commands");
                    return;
                }

                String resultMessage = this.lookup(recive[1]);
                send(resultMessage, port, adress);


            } else {
                System.out.println("Invalid Commands");
                return;
            }
        }

    }

    public void send(String result, int port, InetAddress address) throws IOException {

        DatagramPacket packet = new DatagramPacket(result.getBytes(), result.getBytes().length, address, port);
        serverSocket.send(packet);
        System.out.println("SENDED: "+ result);

    }

    public String registe(String plate, String name) {

        if (dataBase.get(plate) == null) {
            dataBase.put(plate, name);
            System.out.println("registered");
            return "REGISTERED";
        } else {
            System.out.println("error");
            return "ERROR";
        }
    }

    public String lookup(String plate) {

        if (dataBase.get(plate) == null) {
            System.out.println("error");
            return "ERROR";
        } else {
            String result = dataBase.get(plate).toString();
            System.out.println("plate: " + result);
            return result;
        }
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        try {
            Server2 myServer = new Server2(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MulticastThread extends Thread {
        private MulticastThread() {
        }

        public void run() {
            String message = Integer.toString(servicePort);
            System.out.println(addr + "  " + servicePort);
            System.out.println(message);

            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, addr, multicastPort);
                    multicastSocket.send(packet);
                    System.out.println("enviei");
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException end) {
                    System.out.println("erro");
                }
            }
        }
    }

}

