package udp;

import files.Disk;
import protocols.*;

import java.io.IOException;

import java.net.*;

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class Server implements Runnable {

    private String version;
    private int idSender;
    private String acessPoint;


    private int BUF_LENGTH = 65000;

    protected MulticastBackup MDB;
    protected MulticastControl MC;
    protected MulticastRestore MDR;
    protected Disk diskServer;

    @Override
    public void run() {

    }

    public Server(String version, int idSender, String acessPoint, String MControl, int PortControl, String MBackup, int PortBackup, String MRestore, int PortRestore) throws InterruptedException, IOException {


        this.version = version;
        this.idSender = idSender;
        this.acessPoint = acessPoint;
        this.diskServer= new Disk();


        MDB = new MulticastBackup(PortBackup, MBackup, idSender, this);
        MC = new MulticastControl(PortControl, MControl, idSender, this);
        MDR = new MulticastRestore(PortRestore, MRestore, idSender, this);


        Thread MC_Thread = new Thread(MC);
        MC_Thread.start();

        Thread MDB_Thread = new Thread(MDB);
        MDB_Thread.start();

        Thread MDR_Thread = new Thread(MDR);
        MDR_Thread.start();


        if (idSender == 1) {
            // String message=MC.messageDelete("1.0",idSender,"1");
            String message = MC.messageGetChunk("1.0", idSender, "1", 1);
            MC.sendsMessage(message);
        }

    }

    public void sendForRestore(String message) {
        this.MDR.sendsMessage(message);
    }

    public void sendForBackup(String message) {
        this.MDB.sendsMessage(message);
    }

    public void sendForControl(String message) {
        this.MC.sendsMessage(message);
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            //java udp.Server "1.0" 1 "acessPoint" "228.5.6.7" 3000 "228.5.6.6" 4000 "228.5.6.8" 5000
            //java udp.Server "1.0" 2 "acessPoint" "228.5.6.7" 3000 "228.5.6.6" 4000 "228.5.6.8" 5000


            Server server = new Server(args[0], Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6]), args[7], Integer.parseInt(args[8]));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

