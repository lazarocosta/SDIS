package udp;

import protocols.Message;

import java.io.IOException;

import java.net.*;

// run server 3000 228.5.6.7 4445

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class Server implements Runnable {

    private InetAddress mControl;
    private InetAddress mBackup;
    private InetAddress mResture;

    private int portControl;
    private int portBackup;
    private int portResture;

    private MulticastSocket sControl;
    private MulticastSocket sBackup;
    private MulticastSocket sResture;


    private int BUF_LENGTH = 2000;

    @Override
    public void run() {

    }

    public Server(String MControl, int PortControl, String MBackup, int PortBachup, String MResture, int PortResture) throws UnknownHostException, InterruptedException, IOException {


        this.mControl = InetAddress.getByName(MControl);
        this.mBackup = InetAddress.getByName(MBackup);
        this.mResture = InetAddress.getByName(MResture);

        this.portBackup = PortBachup;
        this.portControl = PortControl;
        this.portResture = PortResture;

        try {
            System.out.println("espero");
            sControl = new MulticastSocket(portControl);

            sBackup = new MulticastSocket(portBackup);
            sBackup.joinGroup(mControl);

            byte[] recive = new byte[BUF_LENGTH];
            DatagramPacket datagramPacketRecive = new DatagramPacket(recive, recive.length);
            sBackup.receive(datagramPacketRecive);
            System.out.println("recive");

            this.send();
        } catch (IOException A) {
            A.printStackTrace();
        }


    }

    public void send() throws IOException {


        Message messageLine = new Message("vers", 1, "file", 1, 2);
        String message = messageLine.generatePutChunkLine();

        DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, mControl, portControl);
        sControl.send(datagramPacketSend);
        System.out.println("enviou");

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            Server server = new Server("228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

