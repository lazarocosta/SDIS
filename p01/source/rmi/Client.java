package rmi;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private Client() {
    }

    public static void main(String[] args) throws IOException {

        /* NAO APAGAR********************     NAO APAGAR  TESTE

        System.out.println("response: ");
        File f = new File("aaaaa");
        f.mkdirs();
        OutputStream os = new FileOutputStream("aaaaa/qq.txt");
        String string = "abcdefgijklmnopkrstuvwxyz";

        byte bWrite[] = string.getBytes();
        for (int x = 0; x < bWrite.length; x++) {
            os.write(bWrite[x]);   // writes the bytes
        }
        os.close();
        System.out.println("response: ");


        InputStream is = new FileInputStream("aaaaa/qq.txt");
        int size = is.available();

        for (int i = 0; i < size; i++) {
            System.out.print((char) is.read() + "  ");
        }
        */


        File file = null;
        String[] paths;
        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry(host);   // Gets the server's host address
            Service stub = (Service) registry.lookup("RMI");  // Looks up for the stub with the "Service" name binded


            File f = new File("test.txt");
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
