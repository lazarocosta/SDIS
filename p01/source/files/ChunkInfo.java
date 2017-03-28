package files;

import java.io.Serializable;
import java.util.Objects;

/**
 * ChunkInfo contains the metadata information of a Chunk: its fileId and its chunkNo, which define an unique chunk.
 *
 * It is serializable so it can be easily stored on any Peer.
 */
public class ChunkInfo implements Serializable {

    private String fileId;
    private int chunkNo;

    public ChunkInfo(String fileId, int chunkNo) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    /**
     * Whenever a.equals(b), then a.hashCode() must be same as b.hashCode().
     *
     * In practice:
     *
     * If you override one, then you should override the other.
     * Use the same set of fields that you use to compute equals() to compute hashCode().
     *
     * From: https://www.mkyong.com/java/java-how-to-overrides-equals-and-hashcode/ section 2.:
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == this)
            return true;    // if it is the same reference, it is the same object

        if (!(obj instanceof ChunkInfo))
            return false;   // if it is not a Chunk object, it can't be the same object

        ChunkInfo c = (ChunkInfo) obj;

        return chunkNo == c.chunkNo &&
                Objects.equals(fileId, c.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }

    @Override
    public String toString() {
        return "ChunkInfo[fileID: " + this.fileId + ", chunkNo: " + this.chunkNo + "]";
    }

    // For testing
    public static void main(String[] args) {

    }
}
