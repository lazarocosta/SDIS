package app;

/**
 *
 */
public class TestAppCommands {

    public static final String BACKUP = "BACKUP";
    public static final String RESTORE = "RESTORE";
    public static final String DELETE = "DELETE";
    public static final String RECLAIM = "RECLAIM";
    public static final String STATE = "STATE";

    public static String USAGE_BACKUP = "java TestApp <peer_ap> " + BACKUP + " <file_path_name> <replication_degree>";
    public static String USAGE_RESTORE = "java TestApp <peer_ap> " + RESTORE + " <file_path_name>";
    public static String USAGE_DELETE = "java TestApp <peer_ap> " + DELETE + " <file_path>";
    public static String USAGE_RECLAIM = "java TestApp <peer_ap> " + RECLAIM + " <space_to_reclaim(kB)>";
    public static String USAGE_STATE = "java TestApp <peer_ap> " + STATE;

    public static String[] usages = {USAGE_BACKUP, USAGE_DELETE, USAGE_RESTORE, USAGE_RECLAIM, USAGE_STATE};


    public static String getCommand(String command)
    {
        if(command.equalsIgnoreCase(BACKUP))
            return BACKUP;
        if(command.equalsIgnoreCase(RESTORE))
            return RESTORE;
        if(command.equalsIgnoreCase(DELETE))
            return DELETE;
        if(command.equalsIgnoreCase(RECLAIM))
            return RECLAIM;
        if(command.equalsIgnoreCase(STATE))
            return STATE;

        return null;
    }

    public static void printUsage(){
        System.out.println("Usage: \n");

        for(String usage : usages)
        {
            System.out.println(usage);
        }
    }

}
