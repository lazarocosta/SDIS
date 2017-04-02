package files;

import chunk.Chunk;
import chunk.ChunkInfo;

import java.util.HashMap;

/**
 * Created by jazz on 02-04-2017.
 */
public class StoredChunksDatabase {

    private HashMap<ChunkInfo, byte[]> storedChunks;

    StoredChunksDatabase()
    {
        this.storedChunks = new HashMap<>();
    }

    public void addChunk(Chunk c){

        this.storedChunks.put(c.getChunkInfo(), c.getData());
    }

    public void removeChunk(ChunkInfo info){
        this.storedChunks.remove(info);
    }
}
