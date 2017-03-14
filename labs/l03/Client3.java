import java.net.*;
import java.io.*;

public class Client3 {
    private String operation;
    private String plateNumber;
    private String ownerName;
    private String host_name;
    private int port_number;
    private InetAddress address;
    private Socket echoSocket;
    private PrintWriter out;
    private BufferedReader in;

    public static void main(String[] args) throws UnknownHostException {
        try {
            Client3 myClient = new Client3(args);
            myClient.waitResponse();
            myClient.close();
        } catch (IOException A) {
            A.fillInStackTrace();
        }

    }


    public Client3(String[] args) throws UnknownHostException {


        if (args.length < 3) {
            System.out.println("There is missing an arguments");
            return;
        }
        host_name = args[0];
        port_number = Integer.parseInt(args[1]);
        operation = args[2];
        address = InetAddress.getByName(host_name);
        String message;


        try {
            echoSocket = new Socket(address, port_number);

            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));


            if (operation.equals("register") | operation.equals("REGISTER")) {

                if (args.length != 5) {
                    System.out.println("Bad sintactic, expected:  <host_name> <port_number> <oper> <plate number> <owner name>");
                    return;
                }
                plateNumber = args[3];
                ownerName = args[4];
                message = "REGISTER " + plateNumber + " " + ownerName;

            } else if (operation.equals("lookup") | operation.equals("LOOKUP")) {

                if (args.length != 4) {
                    System.out.println("Bad sintactic, expected:  <host_name> <port_number> <oper> <plate number>");
                    return;
                }
                plateNumber = args[3];
                message = "LOOKUP " + plateNumber;

            } else {
                System.out.println("Bad argument for 'operation.'");
                return;
            }

            out.println(message);
            System.out.println("Sended to : " + message);

        } catch (IOException A) {
            A.fillInStackTrace();
        }


    }

    public void waitResponse() throws IOException {

        String recived = in.readLine();
        System.out.print(recived);

    }

    public void close() throws IOException {

        out.close();
        in.close();
    }


}
