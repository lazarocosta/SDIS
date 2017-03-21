package chunk;

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
     */
    @Override
    public int hashCode() {

        int primeNumber = 31;

        int result = 17;    // making a new hashCode using primes 17 and 31
        result = primeNumber * result + fileId.hashCode();  //
        result = primeNumber * result + chunkNo;

        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)    // if the reference is the same
            return true;

        if (obj == null)    // if obj is null
            return false;

        if (getClass() != obj.getClass())   // if they're not the same class
            return false;

        Chunk c = (Chunk) obj;  // compare with the other Chunk

        if (chunkNo != c.chunkNo)   // different No means different Chunk
            return false;

        if (fileId == null) {
            if (c.fileId != null)
                return false;   // comparing null with a chunk should return false

        } else if (!fileId.equals(c.fileId))
            return false;       // if the fileId is different, then so is the Chunk

        return true;
    }
}
