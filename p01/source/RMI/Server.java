package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * TO RUN THE SERVER:
 *
 * - Run 'rmiregistry &' (default port 1099) OR 'rmiregistry <port> &'.
 * - javac Server
 */

/**
 * The Server class implements the 'Hello' interface in which ALL THE REMOTE METHODS SHOULD BE DECLARED. A method not declared in the 'Remote' interface may not be invoked remotely, but it may still be used locally (on the same computer).
 */
public class Server implements Hello {

    static int DEFAULT_REGISTRY_PORT = 1099;

    String serverName;
    Registry serverRegistry;

    public Server(String serverName) throws AlreadyBoundException, RemoteException {
        this.serverName = serverName;

        this.createRegistry();
    }

    public String sayHello() {
        return "Hello, world!";
    }

    public static void main(String args[]) {

        try {
            Server server = new Server("RMI");

            System.err.println("Server ready");

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * This function creates and binds a RMI registry to the server, through which the clients will communicate.
     *
     * @param port  Port on which the remote object is.
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    private void createRegistry(int port) throws RemoteException, AlreadyBoundException {
        /**
         * A stub, in this context, means a mock implementation.
         * That is, a simple, fake implementation that conforms to the interface and is to be used for testing.
         * This allows the code to be tested without dealing with external dependencies.
         */
        Hello stub = (Hello) UnicastRemoteObject.exportObject(this, 0);

        // Bind the remote object's stub in the registry
        Registry registry = LocateRegistry.createRegistry(port);
        registry.bind(this.serverName, stub);

        /**
         * Java RMI provides a registry API for applications to bind a name to a remote object's stub and for clients to look up remote objects by name in order to obtain their stubs.
         * So in this case, the stub is binded with the name "Hello", which clients may search for.
         *
         * The static method LocateRegistry.getRegistry that takes no arguments returns a stub that implements the remote interface java.rmi.registry.Registry and sends
         * invocations to the registry on server's local host on the default registry port of 1099.
         * The bind method is then invoked on the registry stub in order to bind the remote object's stub to the name "Hello" in the registry.
         *
         */

        this.serverRegistry = registry;
    }

    /**
     * Default bindRegistry which binds to default port 1099.
     *
     * @throws AlreadyBoundException
     * @throws RemoteException
     */
    private void createRegistry() throws AlreadyBoundException, RemoteException {
        this.createRegistry(DEFAULT_REGISTRY_PORT);
    }

    /**
     * Unbinds server from registry, freeing it.
     *
     * @throws RemoteException
     */
    public void exit() throws RemoteException
    {
        try{
            // Unregister ourself
            this.serverRegistry.unbind(serverName);

            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);

            System.out.println("Server exiting.");
        }
        catch(Exception e){

        }
    }
}