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

    private String version;
    private int idServer;
    private String acessPoint;

    private InetAddress mControl;
    private InetAddress mBackup;
    private InetAddress mRestore;

    private int portControl;
    private int portBackup;
    private int portRestore;

    private MulticastSocket sControl;
    private MulticastSocket sBackup;
    private MulticastSocket sRestore;


    private int BUF_LENGTH = 65000;

    @Override
    public void run() {

    }

    public Server(String version, int idServer, String acessPoint, String MControl, int PortControl, String MBackup, int PortBackup, String MRestore, int PortRestore) throws InterruptedException, IOException {

        this.version = version;
        this.idServer = idServer;
        this.acessPoint = acessPoint;

        this.mControl = InetAddress.getByName(MControl);
        this.mBackup = InetAddress.getByName(MBackup);
        this.mRestore = InetAddress.getByName(MRestore);

        this.portBackup = PortBackup;
        this.portControl = PortControl;
        this.portRestore = PortRestore;


        try {
            sControl = new MulticastSocket(portControl);
            sBackup = new MulticastSocket(portBackup);
            sRestore = new MulticastSocket(portRestore);

            sRestore.joinGroup(mRestore);
            sBackup.joinGroup(mBackup);
            sControl.joinGroup(mControl);

        } catch (IOException A) {
            A.printStackTrace();
        }
        MulticastControl multicastControl = new MulticastControl();

        multicastControl.start();

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            Server server = new Server("1.0", 2, "acessPoint", "228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class MulticastRestore extends Thread {

        public void sends(String message) {
            try {
                //Message messageLine = new Message("vers", 1, "file", 1, 2);
                //String message = messageLine.generatePutChunkLine();

                DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, mControl, portControl);
                sRestore.send(datagramPacketSend);
                System.out.println("sends message");

            } catch (IOException A) {
                A.printStackTrace();
            }
        }

        public void receive() {

            System.out.println("MulticastRestore");

            while (true) {
                try {
                    byte[] receive = new byte[BUF_LENGTH];
                    DatagramPacket datagramPacketReceive = new DatagramPacket(receive, receive.length);
                    sRestore.receive(datagramPacketReceive);
                    String messageComplete = new String(datagramPacketReceive.getData(), 0, datagramPacketReceive.getLength());

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

        public void sends(String message) {
            try {
                //Message messageLine = new Message("vers", 1, "file", 1, 2);
                //String message = messageLine.generatePutChunkLine();

                DatagramPacket datagramPacketSend = new DatagramPacket(message.getBytes(), message.getBytes().length, mControl, portControl);
                sBackup.send(datagramPacketSend);
                System.out.println("sends message");

            } catch (IOException A) {
                A.printStackTrace();
            }
        }

        public void receive() {
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

