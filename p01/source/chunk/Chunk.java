package chunk;

import java.util.Objects;

public class Chunk {

    public static final int MAX_SIZE = 64000;   // 64 KByte

    private String fileId;
    private int chunkNo;
    private int replicationDegree;
    private byte[] data;

    public Chunk(String fileId, int chunkNo, int replicationDegree, byte[] data)
    {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.replicationDegree = replicationDegree;
        this.data = data;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "FileID: " +  fileId + ", ChunkNo: " +  chunkNo;
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

        if (!(obj instanceof Chunk))
            return false;   // if it is not a Chunk object, it can't be the same object

        Chunk c = (Chunk) obj;

        return chunkNo == c.chunkNo &&
                Objects.equals(fileId, c.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }
}
