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
		InetAddress addr = InetAddress.getByName(multicastAddress);

		// Open a new DatagramSocket, which will be used to send the data.
		try (DatagramSocket serverSocket = new DatagramSocket()) {

			for (int i = 0; i < 5; i++) {

				String msg = "Sent message no " + i;

				// Create a packet that will contain the data

				// (in the form of bytes) and send it.

				DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, this.multicastPort);

				serverSocket.send(msgPacket);

				System.out.println("Server sent packet with msg: " + msg);

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