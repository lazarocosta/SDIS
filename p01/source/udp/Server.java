package udp;

import protocols.*;

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


    private int BUF_LENGTH = 65000;
    private int ATTEMPTS = 5;

    private MulticastBackup MDB;
    private MulticastControl MC;
    private MulticastRestore MDR;

    @Override
    public void run() {

    }

    public Server(String version, int idServer, String acessPoint, String MControl, int PortControl, String MBackup, int PortBackup, String MRestore, int PortRestore) throws InterruptedException, IOException {


        this.version = version;
        this.idServer = idServer;
        this.acessPoint = acessPoint;


        MDB = new MulticastBackup(PortBackup, MBackup);
        MC = new MulticastControl(PortControl, MControl);
        MDR = new MulticastRestore(PortRestore, MRestore);


        //Init all threads
        Thread MC_Thread = new Thread(MC);
        MC_Thread.start();

        Thread MDB_Thread = new Thread(MDB);
        MDB_Thread.start();

        Thread MDR_Thread = new Thread(MDR);
        MDR_Thread.start();

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            Server server = new Server("1.0", 2, "acessPoint", "228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

