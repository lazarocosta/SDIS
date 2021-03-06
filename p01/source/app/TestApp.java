package app;

import rmi.ServiceInterface;
import systems.Peer;

import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class to be run when testing the app.
 */
public class TestApp {

    private static final String rmiHost = "localhost";

    private enum operations {BACKUP, RESTORE, DELETE, RECLAIM};

    private static String peer_ap;
    private static String sub_protocol;
    private static String command;
    private static String filePath;
    private static Integer replication_degree, size;

    private static ServiceInterface stub;

    // java app.TestApp
    public static void main(String[] args) throws RemoteException {

        System.out.println("Test App running...");

        if (!verifyArgs(args)) return;

        System.out.println("Arguments accepted.");

        switch (command) {
            case TestAppCommands.BACKUP:
                System.out.println("Executing backup of file '" + filePath + "' with replication degree equal to " + replication_degree);
                stub.backupFile(filePath, replication_degree);
                break;

            case TestAppCommands.RESTORE:
                System.out.println("Executing restore of file '" + filePath);
                stub.restoreFile(filePath);
                break;

            case TestAppCommands.DELETE:
                System.out.println("Executing deletion of file '" + filePath);
                stub.deleteFile(filePath);
                break;

            case TestAppCommands.RECLAIM:
                System.out.println("Reclaiming '" +  size + "' kBytes.");
                stub.reclaim(size);
                break;

            case TestAppCommands.STATE:
                System.out.println("Showing peer state.");
                stub.state();
                break;

            default:
                break;
        }

    }

    public static boolean verifyArgs(String[] args) {


        if (args.length == 0) {
            TestAppCommands.printUsage();
            return false;
        }


        if (args[0].equals("help")) {
            TestAppCommands.printUsage();
            return false;
        }

        if (args.length < 2) {
            System.out.println("Missing arguments");
            TestAppCommands.printUsage();
            return false;
        }

        peer_ap = args[0];
        sub_protocol = args[1];

        try {
            System.out.println("Connecting to RMI Host:  '" + rmiHost + "' on AP '" + peer_ap +"'.");
            // TODO: FIX THIS WHEN FINISHED TESTING
            Registry registry = LocateRegistry.getRegistry();

            stub = (ServiceInterface) registry.lookup(peer_ap);


        } catch (RemoteException | NotBoundException e) {
            System.err.println("Invalid RMI object name.");
            e.printStackTrace();
            return false;
        }

        if ((command = TestAppCommands.getCommand(sub_protocol)) == null) {
            System.out.println("Unknown command.");
            TestAppCommands.printUsage();
            return false;
        }

        switch (command) {
            case TestAppCommands.BACKUP:
                if (args.length != 4) {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                if ((filePath = validPathArg(args[2])) == null) {
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                if ((replication_degree = validIntegerArg(args[3])) == null) {
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                break;

            case TestAppCommands.RESTORE:
                if (args.length != 3) {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_RESTORE + "\n");
                    return false;
                }

                filePath = args[2];

                break;

            case TestAppCommands.DELETE:
                if (args.length != 3) {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_DELETE + "\n");
                    return false;
                }

                filePath = args[2];

                break;

            case TestAppCommands.RECLAIM:
                if (args.length != 3) {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_RECLAIM + "\n");
                    return false;
                }

                if ((size = validIntegerArg(args[2])) == null) {
                    System.out.println(TestAppCommands.USAGE_DELETE + "\n");
                    return false;
                }

                break;

            case TestAppCommands.STATE:
                if (args.length != 2) {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_STATE + "\n");
                    return false;
                }


                break;

            default:
                System.err.println("Unknown command");

                TestAppCommands.printUsage();

                return false;
        }

        return true;
    }

    private static String validPathArg(String arg) {
        File file = new File(arg);

        if (!file.exists()) {
            System.err.println(file.getAbsolutePath() + " is not a valid path.");

            return null;

        } else if (!file.isFile()) {
            System.err.println(file.getAbsolutePath() + " is not a binary file.");
            return null;
        }

        return arg;
    }

    private static Integer validIntegerArg(String arg) {
        Integer ret;

        try {
            ret = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            System.err.println("Argument must be a valid integer number.");

            e.printStackTrace();

            ret = null;
        }

        return ret;
    }
}

