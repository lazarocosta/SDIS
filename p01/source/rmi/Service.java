package rmi;

import files.Chunk;
import files.MyFile;
import channels.ChannelGroup;
import protocol.Backup;
import protocol.Delete;
import protocol.Message;
import protocol.SubProtocol;
import systems.Peer;

import java.io.File;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.copyOfRange;

/**
 * TO RUN THE SERVER:
 * <p>
 * - Run 'rmiregistry &' (default port 1099) OR 'rmiregistry <port> &'.
 * - javac ChannelGroup
 */

/**
 * The ChannelGroup class implements the 'ServiceInterface' interface in which ALL THE REMOTE METHODS SHOULD BE DECLARED. A method not declared in the 'Remote' interface may not be invoked remotely, but it may still be used locally (on the same computer).
 */
public class Service implements ServiceInterface {

    private static int DEFAULT_REGISTRY_PORT = 1099;

    // TODO: Allow to run 2 servers on same Port. Use exceptions to see if port is already in use.

    String accessPoint;
    Registry serverRegistry;

    Map<String, String> fileIdToFileName = new HashMap<>();
    private int ATTEMPTS = 5;
    private long SLEEP_ms = 50;


    public Service(String accessPoint) throws AlreadyBoundException, IOException, InterruptedException {

        this.accessPoint = accessPoint;
        //this.createRegistry();
    }

    @Override
    public void backupFile(String  path, int replicationDegree) throws RemoteException{

        File file = new File(path);
        ArrayList<Chunk> chunks = null;
        MyFile myFile = new MyFile(replicationDegree);
        String fileId = null;
        try {
            fileId = myFile.generateFileId(file);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            chunks = myFile.divideFileIntoChunks(file, fileId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("chunks size " + chunks.size());

        for (int i = 0; i < chunks.size(); i++) {

            int timeout = 1000;
            int chunkNo = chunks.get(i).getChunkNo();
            Message message = new Message( Peer.getSenderId(), fileId, chunkNo, replicationDegree);
            message.setBody(chunks.get(i).getData());

            // System.out.println(chunks.get(i).getData());
            String msg = message.msgPutChunk();

          /*  for (int j = 0; j < 5; j++) {
                this.MDB.sendsMessage(msg);

                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException ex) {

                }
              if (MC.count_reply >= replication) {
                    break;
                }

               // timeout *= 2;
            }*/
        }
        System.out.println(fileIdToFileName);
    }

    @Override
    public void restoreFile(String filePath) throws RemoteException {

    }

    @Override
    public void deleteFile(String filePath) throws RemoteException {

        String fileId = Peer.getDb().getBackedUpFiles().get(filePath);  // gets fileId from filePath

        Delete.deleteFile(fileId);

        int attempts = ATTEMPTS;

        while (attempts > 0) {

            String msgDelete = Peer.getUdpChannelGroup().getMC().messageDelete(Peer.getSenderId(), fileId);
            Peer.getUdpChannelGroup().getMC().sendsMessage(msgDelete);

            try {
                Thread.sleep(SLEEP_ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attempts--;
        }
    }

    @Override
    public void reclaim(int amount) throws RemoteException {

    }

    @Override
    public void exit() throws RemoteException {
        try {
            // Unregister the RMI
            this.serverRegistry.unbind(accessPoint);

            // Un-export; this will also remove us from the rmi runtime
            UnicastRemoteObject.unexportObject(this, true);

            System.out.println("ChannelGroup exiting.");
        } catch (Exception e) {

        }
    }

    /**
     * This function creates and binds a rmi registry to the server, through which the clients will communicate.
     *
     * @param port Port on which the remote object is.
     * @throws RemoteException
     */
    private void createRegistry(int port) throws RemoteException {
        /**
         * A stub, in this context, means a mock implementation.
         * That is, a simple, fake implementation that conforms to the interface and is to be used for testing.
         * This allows the code to be tested without dealing with external dependencies.
         */
        ServiceInterface stub = (ServiceInterface) UnicastRemoteObject.exportObject(this, 0);

        // Bind the remote object's stub in the registry
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(port);
            registry.bind(this.accessPoint, stub);
        }
        catch (ExportException e) {
            System.out.println("Export exception when creating RMI port.");
            try {
                registry = LocateRegistry.getRegistry(port);
            }
            catch(ExportException e2)
            {
                System.out.println("Export exception when getting RMI port.");
                return;
            }
        }
        catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

        /**
         * Java rmi provides a registry API for applications to bind a name to a remote object's stub and for clients to look up remote objects by name in order to obtain their stubs.
         * So in this case, the stub is binded with the name "ServiceInterface", which clients may search for.
         *
         * The static method LocateRegistry.getRegistry that takes no arguments returns a stub that implements the remote interface java.rmi.registry.Registry and sends
         * invocations to the registry on server's local host on the default registry port of 1099.
         * The bind method is then invoked on the registry stub in order to bind the remote object's stub to the name "ServiceInterface" in the registry.
         *
         */

        this.serverRegistry = registry;
    }

    /**
     * Default bindRegistry which binds to default port 1099.
     *
     * @throws RemoteException
     */
    private void createRegistry() throws RemoteException {
        this.createRegistry(DEFAULT_REGISTRY_PORT);
    }

    public static void main(String args[]) {

    }

}