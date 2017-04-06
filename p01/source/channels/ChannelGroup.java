package channels;


import rmi.Service;
import rmi.ServiceInterface;

import java.io.IOException;

import java.net.*;
import java.rmi.AlreadyBoundException;
import java.util.Arrays;

/**
 * Multicast addresses: 224.0.0.0 to 239.255.255.255. Best to use 224-238 which are not reserved for anything.
 */

public class ChannelGroup {

    protected MulticastBackup MDB;
    protected MulticastControl MC;
    protected MulticastRestore MDR;


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

    public void sendForRestore(byte[] packet) {
        System.out.println("Sent packet:" + Arrays.toString(packet));
        this.MDR.sendsMessage(packet);
    }

    public void sendForBackup(byte[] packet) {
        this.MDB.sendsMessage(packet);
    }

    public void sendForControl(byte[] packet) {
        this.MC.sendsMessage(packet);
    }

    public static void main(String[] args) throws IOException, InterruptedException, AlreadyBoundException {


    }
}



