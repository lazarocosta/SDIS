package channels;


import rmi.Service;
import rmi.ServiceInterface;

import java.io.IOException;

import java.net.*;
import java.rmi.AlreadyBoundException;

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

    public static void main(String[] args) throws IOException, InterruptedException, AlreadyBoundException {


    }
}



