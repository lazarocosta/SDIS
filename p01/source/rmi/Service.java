package rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The 'Service' interface allows objects to use the 'Remote' interface, which lets remote methods be invoked.
 *
 * A remote method is a method that is called by one computer to make another computer do some task. Normally, it envolves passing objects between computers in a serialized way.
 *
 */

public interface Service extends Remote {

    // Test function
    String sayHello() throws RemoteException;

    // Service functions
    void backupFile(File file, int replicationDegree) throws RemoteException;

    void restoreFile(File file) throws RemoteException;

    void deleteFile(File file) throws RemoteException;

    void freeSpace(int amount) throws RemoteException;

    void exit() throws RemoteException;
}