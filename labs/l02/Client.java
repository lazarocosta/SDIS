import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Client {

	private String multicastAddress;
	private int multicastPort;
	private String operation;

	public void run(String[] args) throws UnknownHostException {

		this.multicastAddress = args[0];
		this.multicastPort = Integer.parseInt(args[1]);
		this.operation = args[2]; // "register" or "lookup"

		String message;

		switch (operation){
		 case "register": {
			if (args.length == 4) {
				System.out.println("There is missing an argument for 'register' function.");
				return;
			}

			String plateNumber = args[3];
			String ownerName = args[4];

			message = "REGISTER " + plateNumber + " " + ownerName;
		 }
			break;

		 case "lookup": {
			if (args.length == 5) {
				System.out.println("There is an extra argument for 'lookup' function.");
				return;
			}

			String plateNumber = args[3];
			message = "LOOKUP " + plateNumber;
		 }
			break;

		 default:
			System.out.println("Bad argument for 'operation.'");
			return;
		}

		// Get the address that we are going to connect to.
		InetAddress address = InetAddress.getByName(this.multicastAddress);

		// Create a buffer of bytes, which will be used to store
		// the incoming bytes containing the information from the server.
		// Since the message is small here, 256 bytes should be enough.
		byte[] buf = new byte[256];

		// Create a new Multicast socket (that will allow other sockets/programs
		// to join it as well.
		try (MulticastSocket clientSocket = new MulticastSocket(this.multicastPort)) {

			//Joint the Multicast group.
			clientSocket.joinGroup(address);

			while (true) {
				// Receive the information and print it.
				DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
				clientSocket.receive(msgPacket);
				String msg = new String(buf, 0, buf.length);
				System.out.println("Socket 1 received msg: " + msg);

			}
		} catch (IOException ex) {

			ex.printStackTrace();
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