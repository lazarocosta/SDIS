package files;

import chunk.Chunk;
import chunk.ChunkInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jazz on 02-04-2017.
 */
public class StoredChunksDatabase implements Serializable {

    private Map<ChunkInfo, byte[]> storedChunks;
    private Map<ChunkInfo, Integer> desiredReplication;
    private Map<ChunkInfo, Integer> obtainedReplication;

    StoredChunksDatabase() {
        this.storedChunks = new HashMap<>();
        this.desiredReplication = new HashMap<>();
        this.obtainedReplication = new HashMap<>();
    }

    public void addChunk(Chunk c) {
        this.storedChunks.put(c.getChunkInfo(), c.getData());
        this.desiredReplication.put(c.getChunkInfo(), c.getReplicationDegree());

        this.incrementReplicationObtained(c.getChunkInfo());
    }

    public void removeChunk(ChunkInfo chunkInfo) {
        this.storedChunks.remove(chunkInfo);
        this.desiredReplication.remove(chunkInfo);

        this.decrementReplicationObtained(chunkInfo);
    }

    public void incrementReplicationObtained(ChunkInfo chunkInfo) {
        if(this.obtainedReplication.containsKey(chunkInfo)) {
            this.obtainedReplication.put(chunkInfo, this.obtainedReplication.get(chunkInfo) + 1);
        }
        else
        {
            this.obtainedReplication.put(chunkInfo, 1);
        }

    }

    public void decrementReplicationObtained(ChunkInfo chunkInfo) {

        if(this.obtainedReplication.containsKey(chunkInfo)) {
            this.obtainedReplication.put(chunkInfo, this.obtainedReplication.get(chunkInfo) - 1);
        }
        else
        {
            this.obtainedReplication.put(chunkInfo, -1);
        }

    }

    public void resetReplicationObtained(ChunkInfo chunkInfo)
    {
        this.obtainedReplication.put(chunkInfo, 0);
    }

    public Map<ChunkInfo, byte[]> getStoredChunks() {
        return storedChunks;
    }

    public boolean existsChunkInfo(ChunkInfo chunkInfo){
        return storedChunks.containsKey(chunkInfo);
    }

    public byte[] getBodyChunk(ChunkInfo chunkInfo){
        return storedChunks.get(chunkInfo);
    }

    public Map<ChunkInfo, Integer> getDesiredReplication() {
        return desiredReplication;
    }

    public Map<ChunkInfo, Integer> getObtainedReplication() {
        return obtainedReplication;
    }

    // Testing
    public static void main(String[] args) {

    }


}
