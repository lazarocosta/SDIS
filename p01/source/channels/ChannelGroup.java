package channels;


import java.io.IOException;

import java.net.*;

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class ChannelGroup implements Runnable {

    protected int senderId;

    protected MulticastBackup MDB;
    protected MulticastControl MC;
    protected MulticastRestore MDR;


    @Override
    public void run() {

    }

    public ChannelGroup(int senderId, String MControl, int PortControl, String MBackup, int PortBackup, String MRestore, int PortRestore) throws InterruptedException, IOException {


        MDB = new MulticastBackup(PortBackup, MBackup, senderId, this);
        MC = new MulticastControl(PortControl, MControl, senderId, this);
        MDR = new MulticastRestore(PortRestore, MRestore, senderId, this);

        Thread MC_Thread = new Thread(MC);
        MC_Thread.start();

        Thread MDB_Thread = new Thread(MDB);
        MDB_Thread.start();

        Thread MDR_Thread = new Thread(MDR);
        MDR_Thread.start();

        if (senderId == 1) {
            // String message=MC.messageDelete("1.0",idSender,"1");
            // String message = MC.messageGetChunk("1.0", idSender, "1", 1);
            //TER em atenção porque canais se manda a mensagem

            String message = MDB.messagePutChunk(senderId, "putchunk", 99, 1, "1111111111".getBytes());
            MDB.sendsMessage(message);
        }
    }

    public MulticastBackup getMDB() {
        return MDB;
    }

    public MulticastControl getMC() {
        return MC;
    }

    public MulticastRestore getMDR() {
        return MDR;
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
//
//        try {
//            //java channels.ChannelGroup "1.0" 1 "acessPoint" "228.5.6.7" 3000 "228.5.6.6" 4000 "228.5.6.8" 5000
//            //java channels.ChannelGroup "1.0" 2 "acessPoint" "228.5.6.7" 3000 "228.5.6.6" 4000 "228.5.6.8" 5000
//
//            ChannelGroup channelGroup = new ChannelGroup(args[0], Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6]), args[7], Integer.parseInt(args[8]));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    }
}



