package Systems;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);   // Gets the server's host address
            Hello stub = (Hello) registry.lookup("RMI");  // Looks up for the stub with the "Hello" name binded

            String response = stub.sayHello();  // Executes the interface of the stub
            System.out.println("response: " + response);

            // The exit() function must be executed or else the server won't end and the registry won't be freed
            stub.exit();
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
