package chunk;

import java.io.Serializable;
import java.util.Objects;

public class Chunk {

    public static final int MAX_SIZE = 64000;   // 64 KByte

    private ChunkInfo chunkInfo;

    private int replicationDegree;
    private byte[] data;
    private int replicationObtained;
    private int size;

    public Chunk(String fileId, int chunkNo, int replicationDegree, byte[] data) {
        this.chunkInfo = new ChunkInfo(fileId, chunkNo);
        this.replicationDegree = replicationDegree;
        this.data = data;
        this.replicationObtained = 0;
    }

    public void addReplication() {
        this.replicationObtained++;
    }

    public void reduceReplication() {
        this.replicationObtained--;
    }

    public String getFileId() { return this.chunkInfo.getFileId(); }

    public int getChunkNo() { return this.chunkInfo.getChunkNo(); }

    public ChunkInfo getChunkInfo() {
        return chunkInfo;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    public byte[] getData() {
        return data;
    }


    @Override
    public String toString() {
        return "FileID: " +  chunkInfo.getFileId() + ", ChunkNo: " +  chunkInfo.getChunkNo() + "Body: " + new String(data);
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

        return c.getChunkInfo().equals(this.chunkInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkInfo);
    }
}
