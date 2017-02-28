import java.net.*;
class UDPClient {
    public static void main(String args[]) throws Exception {
        if (args.length != 4 && args.length != 5){
            System.out.println("Usage: java UDPClient <host_name> <port_number> <oper> <opnd>*");
            return;
        }
        String hostName = args[0];

/* generally "localhost"*/
        String portNumber = args[1];
        String operation = args[2];
/* "register" or "lookup"*/
        String message;

        switch (operation){
            case "register": {
                if (args.length == 4) {
                    System.out.println("There is missing an argument for 'register' function.");
                    return;
                }
                String plateNumber = args[3];
                String ownerName = args[4];
                message = "REGISTER " + plateNumber + " " + ownerName;
            }
                break;

                case "lookup":{
                    if (args.length == 5) {
                        System.out.println("There is an extra argument for 'lookup' function.");
                        return;
                    }
                    String plateNumber2 = args[3];
                    message = "LOOKUP " + plateNumber2;
            }
                break;

            default:
                System.out.println("Bad argument for 'operation.'");
                return;
        }

        System.out.println("EXECUTING: " + message);

        byte[] sendData = message.getBytes();   // byte array to store information to send data

// The socket constrcutor needs no port, because the port information is already in the packet
        DatagramSocket clientSocket = new DatagramSocket();

// Get IP adress by name resolution
        InetAddress IPAddress = InetAddress.getByName(hostName);

// Send packet
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, Integer.parseInt(portNumber));
        clientSocket.send(sendPacket);

// Receive future packet
        byte[] receiveData = new byte[4096];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        System.out.println("Size: " + receiveData.length);
        String receivedMessage = new String(receivePacket.getData());
        System.out.println("FROM SERVER: " + receivedMessage);

        clientSocket.close();
    }
}