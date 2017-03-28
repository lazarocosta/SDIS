package app;

import java.io.File;

/**
 * Class to be run when testing the app.
 */
public class TestApp {

    private enum operations {BACKUP, RESTORE, DELETE, RECLAIM};

    private static String peer_ap;
    private static String sub_protocol;
    private static String  filePath;
    private static Integer replication_degree, size;


    public static void main(String[] args) {

        if(!verifyArgs(args))
            return;

    }

    public static boolean verifyArgs(String[] args) {

        if(args[0] == "help")
        {
            TestAppCommands.printUsage();
            return false;
        }

        String peer_ap = args[0];
        String sub_protocol = args[1];


        String command;

        if((command = TestAppCommands.getCommand(sub_protocol)) == null)
        {
            System.out.println("Unknown command.");
            TestAppCommands.printUsage();
            return false;
        }

        switch (command)
        {
            case TestAppCommands.BACKUP:
                if(args.length != 4)
                {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                if((filePath = validPathArg(args[2])) == null)
                {
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                if((replication_degree = validIntegerArg(args[3])) == null)
                {
                    System.out.println(TestAppCommands.USAGE_BACKUP + "\n");
                    return false;
                }

                break;

            case TestAppCommands.RESTORE:
                if(args.length != 3)
                {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_RESTORE + "\n");
                    return false;
                }

                filePath = args[2];

                break;

            case TestAppCommands.DELETE:
                if(args.length != 3)
                {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_DELETE + "\n");
                    return false;
                }

                filePath = args[2];

                break;

            case TestAppCommands.RECLAIM:
                if(args.length != 3)
                {
                    System.out.println("Wrong number of arguments.");
                    System.out.println(TestAppCommands.USAGE_RECLAIM + "\n");
                    return false;
                }

                if((replication_degree = validIntegerArg(args[2]))== null)
                {
                    System.out.println(TestAppCommands.USAGE_DELETE + "\n");
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

    private static String validPathArg(String arg)
    {
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

    private static Integer validIntegerArg(String arg)
    {
        Integer ret;

        try{
            ret = Integer.parseInt(arg);
        }
        catch (NumberFormatException e)
        {
            System.err.println("Argument must be a valid integer number.");

            e.printStackTrace();

            ret = null;
        }

        return ret;
    }
}

