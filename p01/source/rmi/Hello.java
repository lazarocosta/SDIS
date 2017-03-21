package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The 'Hello' interface allows objects to use the 'Remote' interface, which lets remote methods be invoked.
 *
 * A remote method is a method that is called by one computer to make another computer do some task. Normally, it envolves passing objects between computers in a serialized way.
 *
 */

public interface Hello extends Remote {

    String sayHello() throws RemoteException;

    void exit() throws RemoteException;
}