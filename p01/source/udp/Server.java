package udp;

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
    private int AVAILABLE_SPACE = 124000000;

    private MulticastBackup MDB;
    private MulticastControl MC;
    private MulticastRestore MDR;
    public int availableSpace = AVAILABLE_SPACE;

    @Override
    public void run() {

    }

    public Server(String[] args) throws InterruptedException, IOException {

        String version = args[0];
        int idSender = Integer.parseInt(args[1]);
        String acessPoint = args[2];
        String MControl = args[3];
        int PortControl = Integer.parseInt(args[4]);
        String MBackup = args[5];
        int PortBackup = Integer.parseInt(args[6]);
        String MRestore = args[7];
        int PortRestore = Integer.parseInt(args[8]);


        this.version = version;
        this.idSender = idSender;
        this.acessPoint = acessPoint;


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
            //String message=MC.messageDelete("1.0",idSender,"1");
            String message1 = MC.messageGetChunk("1.0", idSender, "1", 1);
            MC.sendsMessage(message1);
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


            Server server = new Server(args);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

