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
	
    public Server() {}

    public String sayHello() {
	return "Hello, world!";
    }
	
    public static void main(String args[]) {
	
	try {
	    Server obj = new Server();
	    Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

        /**
         * A stub, in this context, means a mock implementation.
         * That is, a simple, fake implementation that conforms to the interface and is to be used for testing. This allows the code to be tested without dealing with external dependencies.
         */

	    // Bind the remote object's stub in the registry
	    Registry registry = LocateRegistry.getRegistry();
	    registry.bind("Hello", stub);

        /**
         * Java RMI provides a registry API for applications to bind a name to a remote object's stub and for clients to look up remote objects by name in order to obtain their stubs.
         * So in this case, the stub is binded with the name "Hello", which clients may search for.
         * 
         * The static method LocateRegistry.getRegistry that takes no arguments returns a stub that implements the remote interface java.rmi.registry.Registry and sends invocations to the registry on server's local host on the default registry port of 1099. The bind method is then invoked on the registry stub in order to bind the remote object's stub to the name "Hello" in the registry.
         * 
         */

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}