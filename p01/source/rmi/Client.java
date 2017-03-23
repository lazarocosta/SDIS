package rmi;

import java.io.File;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);   // Gets the server's host address
            Service stub = (Service) registry.lookup("RMI");  // Looks up for the stub with the "Service" name binded


            File f = new File("/home/jazz/MyRepos/FEUP/3a-2s/SDIS/p01/test/test.txt");
            stub.backupFile(f, 2);

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