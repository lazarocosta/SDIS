import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;

public class Client {

	private String multicastAddress;
	private int multicastPort;
	private String operation;


	public void run(String[] args) throws UnknownHostException {

		this.multicastAddress = args[0];
		this.multicastPort = Integer.parseInt(args[1]);
		this.operation = args[2]; // "register" or "lookup"

		String message;

		switch (operation) {
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
		//InetAddress address = InetAddress.getByName(this.multicastAddress);
        InetAddress address =InetAddress.getByName("228.5.6.7");



		//Send DatagramPacket
		// Create a buffer of bytes, which will be used to store
		// the incoming bytes containing the information from the server.
		// Since the message is small here, 256 bytes should be enough.


		// Create a new Multicast socket (that will allow other sockets/programs
		// to join it as well.
		try (MulticastSocket clientSocket = new MulticastSocket(this.multicastPort)) {

			//Joint the Multicast group.
			clientSocket.joinGroup(address);

			byte[] send = message.getBytes();

			//http://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
			//Send Menssage
			DatagramPacket sendMessa= new DatagramPacket(send, send.length, address, this.multicastPort);
			clientSocket.send(sendMessa);
            System.out.println("Send");

			byte[] buf = new byte[3000];
			//	while (true) {
			// Receive the information and print it.
			DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
			clientSocket.receive(msgPacket);
			String msg = new String(buf, 0, buf.length);
			if(msg.equals("-1"))
				System.out.println("Cliente receiver: "+ message + " :: ERRO");
			else
				System.out.println("Cliente receiver: "+ message + " :: "+ msg);

			clientSocket.leaveGroup(address);
			System.out.println("Leave Group");

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