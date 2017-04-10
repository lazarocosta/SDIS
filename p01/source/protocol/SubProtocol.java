package protocol;

public class SubProtocol {

    private static String version;

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SubProtocol.version = version;
    }
}
