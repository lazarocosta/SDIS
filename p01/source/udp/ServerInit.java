package udp;

import protocols.*;

import java.io.IOException;

import java.net.*;

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class ServerInit implements Runnable {

    private String version;
    private int idServer;
    private String acessPoint;
    private String fileId;


    private int BUF_LENGTH = 65000;
    private int ATTEMPTS = 5;

    private MulticastBackup MDB;
    private MulticastControl MC;
    private MulticastRestore MDR;

    @Override
    public void run() {

    }

    public ServerInit(String[] args) throws InterruptedException, IOException {

        String version = args[0];
        int idServer = Integer.parseInt(args[1]);
        String acessPoint = args[2];
        String MControl = args[3];
        int PortControl = Integer.parseInt(args[4]);
        String MBackup = args[5];
        int PortBackup = Integer.parseInt(args[6]);
        String MRestore = args[7];
        int PortRestore = Integer.parseInt(args[8]);


        this.version = version;
        this.idServer = idServer;
        this.acessPoint = acessPoint;


        MDB = new MulticastBackup(PortBackup, MBackup);
        MC = new MulticastControl(PortControl, MControl);
        MDR = new MulticastRestore(PortRestore, MRestore);


        Thread MC_Thread = new Thread(MC);
        MC_Thread.start();

        Thread MDB_Thread = new Thread(MDB);
        MDB_Thread.start();

        Thread MDR_Thread = new Thread(MDR);
        MDR_Thread.start();

    }

    void delete(String path, MulticastControl MC){

        //cria o fileId
        MC.sendsDelete(version,idServer, fileId);
    }
    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        try {
            //"1.0", 2, "acessPoint", "228.5.6.7", 3000, "228.5.6.6", 4000, "228.5.6.8", 5000
            ServerInit server = new ServerInit(args);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

