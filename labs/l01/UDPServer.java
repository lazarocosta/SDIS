import java.net.*;
import java.util.Map;
import java.util.HashMap;


//RUN com apenas a porta----3000
class UDPServer {

    private Map<String, String> vehicles = new HashMap<String, String>();
    // Map is an interface of HashMap, so a HashMap will have more

    private void addVehicle(String numberPlate, String ownerName) {
        this.vehicles.put(numberPlate, ownerName);
        // numberPlate should be the key, since it is unique. A person may have more than 1 car.
    }

    private String lookupVehicleOwner(String numberPlate) {
        numberPlate = numberPlate.trim();
        System.out.println(this.vehicles);
        System.out.println(numberPlate);

        String owner = this.vehicles.get(numberPlate);//Returns the value to which the specified key is mapped,
        // or null if this map contains no mapping for the key.
        System.out.println(owner);

        return owner;
    }

    public String interpretRequest(String request) {

        String[] splitRequest = request.split("\\s+");  // regex 
        String response;

        switch (splitRequest[0]) {
            case "REGISTER": {
                addVehicle(splitRequest[1], splitRequest[2]);
                response = vehicles.size() + "\n" + splitRequest[1] + " " + splitRequest[2];
            }
            break;

            case "LOOKUP": {
                response = vehicles.size() + "\n" + splitRequest[1] + " " + lookupVehicleOwner(splitRequest[1]);
            }
            break;

            default:
                response = "-1";
        }
        return response;
    }

    public void run(String args[]) throws Exception {
        /** args[] is an array containing the arguments given.
         *  If no argument is given, then args is empty.
         *
         *  For example: java UDPServer arg0 arg1
         */

        if (args.length != 1) {
            System.out.println("Usage: java UPDServer <port_number>");
            return;
        }

        int portGiven = Integer.parseInt(args[0]);

        // Creating a new Datagram Socket on the given port
        DatagramSocket serverSocket = new DatagramSocket(portGiven);
        System.out.println("aqui");

        // Server is always running

        while (true) {
            // A byte array is used to create a Datagram Packet
            byte[] receiveData = new byte[1024];
            byte[] sendData;

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            serverSocket.receive(receivePacket);


            String request = new String(receivePacket.getData());
            System.out.println("RECEIVED: " + request);

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            String response = interpretRequest(request);
            System.out.println("RESPONDING: " + response);
            sendData = response.getBytes();

            System.out.println(port);
            System.out.println("Size: " + sendData.length);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    }


    public static void main(String args[]) throws Exception {
        try {
            UDPServer myServer = new UDPServer();
            myServer.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}