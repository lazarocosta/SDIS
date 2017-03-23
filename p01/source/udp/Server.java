package udp;

import protocols.Message;
import protocols.Delete;
import protocols.Resture;

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

    private int idServer;


    private int BUF_LENGTH = 65000;

    @Override
    public void run() {

    }

    public Server(int idServer, String MControl, int PortControl, String MBackup, int PortBachup, String MResture, int PortResture) throws UnknownHostException, InterruptedException, IOException {


        this.mControl = InetAddress.getByName(MControl);
        this.mBackup = InetAddress.getByName(MBackup);
        this.mResture = InetAddress.getByName(MResture);

        this.portBackup = PortBachup;
        this.portControl = PortControl;
        this.portResture = PortResture;

        this.idServer = idServer;


        try {
            sControl = new MulticastSocket(portControl);
            sBackup = new MulticastSocket(portBackup);
            sResture = new MulticastSocket(portResture);

            sResture.joinGroup(mResture);
            sBackup.joinGroup(mBackup);
            sControl.joinGroup(mControl);

        } catch (IOException A) {
            A.printStackTrace();
        }
        MulticastControl multicastControl = new MulticastControl();

        multicastControl.start();

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
            Server server = new Server(2, "228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MulticastResture extends Thread {

        public void run() {

            System.out.println("MulticastResture");

            while (true) {
                try {
                    byte[] recive = new byte[BUF_LENGTH];
                    DatagramPacket datagramPacketRecive = new DatagramPacket(recive, recive.length);
                    sResture.receive(datagramPacketRecive);
                    String messageComplete = new String(datagramPacketRecive.getData(), 0, datagramPacketRecive.getLength());

                    Message message = new Message(messageComplete);

                    switch (message.msgType) {
                        case "CHUNK": {
                            //recebe os CHUNK
                        }
                        break;
                        default:
                            System.out.println("erro");
                    }

                } catch (IOException A) {
                    A.fillInStackTrace();
                }
            }
        }
    }

    public class MulticastBackup extends Thread {

        public void run() {
            System.out.println("MulticastBackup");

            while (true) {
                try {

                    byte[] recive = new byte[BUF_LENGTH];
                    DatagramPacket datagramPacketRecive = new DatagramPacket(recive, recive.length);
                    sBackup.receive(datagramPacketRecive);
                    String messageComplete = new String(datagramPacketRecive.getData(), 0, datagramPacketRecive.getLength());

                    Message message = new Message(messageComplete);

                    switch (message.msgType) {
                        case "PUTCHUNK": {
                            //faz o backup

                            //envia a resposta pelo Mcontrol
                        }
                        break;


                        default:
                            System.out.println("erro");
                    }

                } catch (IOException A) {
                    A.fillInStackTrace();
                }
            }
        }
    }

    public class MulticastControl extends Thread {

        public void sends(String message) {
            try {
                //Message messageLine = new Message("vers", 1, "file", 1, 2);
                //String message = messageLine.generatePutChunkLine();

                DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, mControl, portControl);
                sControl.send(datagramPacketSend);
                System.out.println("sends message");

            } catch (IOException A) {
                A.printStackTrace();
            }
        }

        public void receive() {

            System.out.println("MulticastControl");

            while (true) {
                try {
                    byte[] recived = new byte[BUF_LENGTH];
                    DatagramPacket datagramPacketRecived = new DatagramPacket(recived, recived.length);
                    sControl.receive(datagramPacketRecived);
                    String messageComplete = new String(datagramPacketRecived.getData(), 0, datagramPacketRecived.getLength());

                    Message message = new Message(messageComplete);


                    switch (message.msgType) {
                        case "GETCHUNK": {
                            //faz o resture
                            //envia a resposta pelor resure
                        }
                        break;
                        case "DELETE": {
                            Delete deleteFile = new Delete();
                            //faz o delete
                        }
                        break;
                        case "REMOVED": {
                            //
                        }
                        break;


                        default:
                            System.out.println("erro");
                    }


                } catch (IOException A) {
                    A.fillInStackTrace();
                }
            }
        }
    }


}

