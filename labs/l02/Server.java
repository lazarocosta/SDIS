import java.io.IOException;
import java.net.*;

// run server 3000 228.5.6.7 4445
public class Server {

    private int servicePort;
    private String multicastAddress;
    private int multicastPort;
    private MulticastSocket multicastSocket;
    private InetAddress addr;

    public void run(String[] args) throws UnknownHostException, InterruptedException, IOException {

        if (args.length != 3) {
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>.");
            return;
        }

        this.servicePort = Integer.parseInt(args[0]);
        this.multicastAddress = args[1];
        this.multicastPort = Integer.parseInt(args[2]);

        multicastSocket = new MulticastSocket();

        // Get the address that we are going to connect to.
        addr = InetAddress.getByName(multicastAddress);

        MulticastThread multicastThread = new MulticastThread();
        multicastThread.start();

        // Open a new DatagramSocket, which will be used to send the data.
        //DatagramSocket serverSocket = new DatagramSocket(this.servicePort);
        DatagramSocket serverSocket = new DatagramSocket(3333);
        while (true) {
            byte[] msg = new byte[2000];
            System.out.println("espero");

            //receiver packet
            DatagramPacket msgPacket = new DatagramPacket(msg, msg.length);
            serverSocket.receive(msgPacket);
            System.out.println("Server receiver packet with msg: " + msg);
        }

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        try {
            Server myServer = new Server();
            myServer.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MulticastThread extends Thread {
        private MulticastThread() {
        }

        public void run() {
            String message = new String(Integer.toString(multicastPort));
            byte[] msg = message.getBytes();
            System.out.println(addr + "  " + servicePort);

            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(msg, msg.length, addr, multicastPort);
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

