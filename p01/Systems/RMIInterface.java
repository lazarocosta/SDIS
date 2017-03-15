package Systems;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RMIInterface extends Remote{

    String sendString() throws RemoteException;

}
