package Systems;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);   // Gets the server's host address
            RMIConnection stub = (RMIConnection) registry.lookup("RMI");  // Looks up for the stub with the "RMI" name binded

            String response = stub.sendString();  // Executes the interface of the stub
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
