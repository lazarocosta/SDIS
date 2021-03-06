package systems;


import files.Database;
import channels.ChannelGroup;
import protocol.SubProtocol;
import rmi.Service;

import java.io.*;
import java.rmi.AlreadyBoundException;

public class Peer {

    private static PeerInfo info; // info that defines the peer
    private static int senderId;

    // RMI interface
    private static Service rmiService;
    private static String accessPoint;

    // UDP Channel Group
    private static ChannelGroup udpChannelGroup;
    private static String controlAddress;
    private static int controlPort;
    private static String dataBackupAddress;
    private static int dataBackupPort;
    private static String dataRestoreAddress;
    private static int dataRestorePort;

    // UDP unicast

    private static Database db;


    // Main method for running a peer
    // args = <version> <senderId> <accessPoint> <IP MC> <Port MC> <IP MDB> <Port MDB> <IP MDR> <Port MDR>
    // args = 1.0 2 accessPoint 228.5.6.7 3000 228.5.6.6 4000 228.5.6.8 5000
    public static void main(String[] args) throws IOException, AlreadyBoundException, InterruptedException {

        if (args.length != 9) {
            System.out.println("Wrong number of arguments.");
            return;
        }

        SubProtocol.setVersion(args[0]);
        senderId = Integer.parseInt(args[1]);
        accessPoint = args[2];
        controlAddress = args[3];
        controlPort = Integer.parseInt(args[4]);
        dataBackupAddress = args[5];
        dataBackupPort = Integer.parseInt(args[6]);
        dataRestoreAddress = args[7];
        dataRestorePort = Integer.parseInt(args[8]);

        loadDatabase();

        rmiService = new Service(accessPoint);
        udpChannelGroup = new ChannelGroup(senderId, controlAddress, controlPort, dataBackupAddress, dataBackupPort, dataRestoreAddress, dataRestorePort);

        System.out.println("Server with id = " + Peer.senderId + " is up and running.");
    }


    public static Database getDb() {
        return db;
    }

    public static int getSenderId() {
        return senderId;
    }

    public static Service getRmiService() {
        return rmiService;
    }

    public static ChannelGroup getUdpChannelGroup() {
        return udpChannelGroup;
    }

    private static void createDatabase() {

        File f = new File(Database.DATA_FILE);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        db = new Database();
        System.out.println("Created new database.");

        saveDatabase();

    }

    public static void saveDatabase(){

        try {

            FileOutputStream fos = new FileOutputStream(Database.DATA_FILE);

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(db);

            fos.close();
        }
        catch (FileNotFoundException e) {

                createDatabase();

            }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loadDatabase(){

        try {
            FileInputStream fis = new FileInputStream(Database.DATA_FILE);

            ObjectInputStream ois = new ObjectInputStream(fis);

            db = (Database) ois.readObject();

            fis.close();

            System.out.println("Loaded database.");

        } catch (FileNotFoundException e) {

            createDatabase();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
