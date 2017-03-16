

import java.net.*;
import java.io.*;
import java.util.HashMap;

// run server 3000 228.5.6.7 4445
public class Server3 {

    private int servicePort;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;
    private HashMap<String, String> dataBase;


    public Server3(String[] args) throws UnknownHostException, InterruptedException, IOException {

        if (args.length != 1) {
            System.out.println("Usage: java Server <srvc_port>");
            return;
        }
        servicePort = Integer.parseInt(args[0]);
        serverSocket = new ServerSocket(servicePort);
        dataBase = new HashMap<>();
    }

    public String registe(String plate, String name) {

        if (dataBase.get(plate) == null) {
            dataBase.put(plate, name);
            System.out.println("registered");
            return "REGISTERED";
        } else {
            System.out.println("error");
            return "ERROR";
        }
    }

    public String lookup(String plate) {

        if (dataBase.get(plate) == null) {
            System.out.println("error");
            return "ERROR";
        } else {
            String result = dataBase.get(plate).toString();
            System.out.println("plate: " + result);
            return result;
        }
    }

    public void start() throws IOException {
        System.out.println("wait");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String comando = in.readLine();
            String[] recive = comando.split("\\s");

            if (recive[0].equals("REGISTER")) {
                if (recive.length != 3) {
                    System.out.println("Invalid number Commands");
                    return;
                }

                String resultMessage = this.registe(recive[1], recive[2]);
                out.println(resultMessage);


            } else if (recive[0].equals("LOOKUP")) {
                if (recive.length != 2) {
                    System.out.println("Invalid number Commands");
                    return;
                }

                String resultMessage = this.lookup(recive[1]);
                out.println(resultMessage);


            } else {
                System.out.println("Invalid Commands");
                return;
            }
        }

    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        try {
            Server3 myServer = new Server3(args);
            myServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

