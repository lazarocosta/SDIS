import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {

	private int servicePort;
	private String multicastAddress;
	private int multicastPort;

	public void run(String[] args) throws UnknownHostException, InterruptedException, IOException {

		if(args.length != 3)
		{
			System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port>.");
			return;
		}

		this.servicePort = Integer.parseInt(args[0]);
		this.multicastAddress = args[1];
		this.multicastPort = Integer.parseInt(args[2]);

		// Get the address that we are going to connect to.
		//InetAddress addr = InetAddress.getByName(multicastAddress);
		InetAddress addr = InetAddress.getByName("228.5.6.7");

		System.out.println("wait");

		// Open a new DatagramSocket, which will be used to send the data.
		try (DatagramSocket serverSocket = new DatagramSocket()) {

			while(true) {

				byte [] msg = new byte[1024];

				//receiver packet
				DatagramPacket msgPacket = new DatagramPacket(msg, msg.length);
				serverSocket.receive(msgPacket);

				System.out.println("Server receiver packet with msg: " + msg);

				Thread.sleep(500);

			}

		} catch (IOException ex) {

			ex.printStackTrace();

		}

	}

	public static void main(String[] args) throws UnknownHostException, InterruptedException {
		try
        {
            Server myServer = new Server();
            myServer.run(args);
        }
        catch (Exception e)
        {
            e.printStackTrace ();
        }

	}

}