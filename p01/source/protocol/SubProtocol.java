package protocol;

/**
 * Created by jazz on 29-03-2017.
 */
public class SubProtocol {

    private static String version;

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        SubProtocol.version = version;
    }
}