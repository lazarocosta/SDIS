import java.net.*;
import java.io.*;

// run server 3000 228.5.6.7 4445
public class Server {

    private int servicePort;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;


    public Server(String[] args) throws UnknownHostException, InterruptedException, IOException {

        if (args.length != 1) {
            System.out.println("Usage: java Server <srvc_port>");
            return;
        }
        servicePort = Integer.parseInt(args[0]);

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException A) {
        }
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        try {
            Server myServer = new Server(args);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

