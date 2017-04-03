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
    private Map<ChunkInfo, Integer> obtainedReplication;

    StoredChunksDatabase() {
        this.storedChunks = new HashMap<>();
        this.obtainedReplication = new HashMap<>();
    }

    public void addChunk(Chunk c) {
        this.storedChunks.put(c.getChunkInfo(), c.getData());
    }

    public void removeChunk(ChunkInfo chunkInfo) {
        this.storedChunks.remove(chunkInfo);
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

    public Map<ChunkInfo, byte[]> getStoredChunks() {
        return storedChunks;
    }

    public Map<ChunkInfo, Integer> getObtainedReplication() {
        return obtainedReplication;
    }

    // Testing
    public static void main(String[] args) {

    }


}
