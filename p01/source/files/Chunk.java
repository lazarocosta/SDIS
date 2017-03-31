package files;

import java.io.Serializable;
import java.util.Objects;

public class Chunk implements Serializable{

    private String fileId;
    private int chunkNo;
    private int replicationDegree;
    private byte[] data;
    private int replicationObtained;
    private int size;

    public Chunk(String fileId, int chunkNo, int replicationDegree, byte[] data) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
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
    public boolean equals(Object obj) {

        if (obj == this) return true;    // if it is the same reference, it is the same object

        if (!(obj instanceof Chunk)) return false;   // if it is not a Chunk object, it can't be the same object

        Chunk c = (Chunk) obj;

        return (chunkNo == c.chunkNo && c.fileId.equals(this.fileId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunkNo, fileId);
    }

    @Override
    public String toString() {
        return "Chunk[fileID: " + this.fileId + ", chunkNo: " + this.chunkNo + ", replication: " + replicationDegree + ", replicationObtained: " + replicationObtained +  "]";
    }

    // For testing
    public static void main(String[] args) {

    }

}
