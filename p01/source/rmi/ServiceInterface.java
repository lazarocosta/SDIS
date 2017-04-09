package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The 'ServiceInterface' interface allows objects to use the 'Remote' interface, which lets remote methods be invoked.
 *
 * A remote method is a method that is called by one computer to make another computer do some task. Normally, it envolves passing objects between computers in a serialized way.
 *
 */

public interface ServiceInterface extends Remote {


    // ServiceInterface functions
    void backupFile(String filePath, int replicationDegree) throws RemoteException;

    void restoreFile(String filePath) throws RemoteException;

    void deleteFile(String filePath) throws RemoteException;

    void reclaim(int amount) throws RemoteException;

    void state() throws RemoteException;


    // Exits the server
    void exit() throws RemoteException;
}