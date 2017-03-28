package systems;

import files.ChunkInfo;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;

/**
 * Class containing the information that defines a Peer. It is serializable so it can be easily exchanged.
 */
public class PeerInfo implements Serializable{

    /**
     * However, it is strongly recommended that all serializable classes explicitly declare serialVersionUID values,
     * since the default serialVersionUID computation is highly sensitive to class details that may vary depending on
     * compiler implementations, and can thus result in unexpected InvalidClassExceptions during deserialization.
     */
    private static final long serialVersionUID = 1L;

    private InetAddress ip;
    private int port;

    public PeerInfo(InetAddress ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this)
            return true;    // if it is the same reference, it is the same object

        if (!(obj instanceof PeerInfo))
            return false;   // if it is not a Chunk object, it can't be the same object

        PeerInfo p = (PeerInfo) obj;

        return port == p.port &&
                Objects.equals(ip, p.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return "PeerInfo[ip: " + this.ip.toString() + ", port: " + this.port + "]";
    }

    // For testing
    public static void main(String[] args) {

    }
}
