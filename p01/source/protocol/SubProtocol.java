package protocol;

public class SubProtocol {

    public static boolean enhancements;

    private static String version;

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SubProtocol.version = version;
    }
}
