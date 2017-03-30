package files;
public class Chunk {

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

}
