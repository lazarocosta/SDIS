package files;

import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jazz on 02-04-2017.
 */
public class StoredChunksDatabase implements Serializable {

    private final String CHUNKS_DIR = "CHUNKS/peer" + Peer.getSenderId()+"/";

    private Map<ChunkInfo, byte[]> storedChunks;
    private Map<ChunkInfo, Integer> desiredReplication;
    private Map<ChunkInfo, Integer> obtainedReplication;

    StoredChunksDatabase() {
        this.storedChunks = new HashMap<>();
        this.desiredReplication = new HashMap<>();
        this.obtainedReplication = new HashMap<>();
    }

    /**
     * Adds a chunk to the volatile memory (hashmap) and also saves its current replication and desired replication.
     * @param c Chunk
     */
    public void addChunk(Chunk c) {
        this.storedChunks.put(c.getChunkInfo(), c.getData());
        this.desiredReplication.put(c.getChunkInfo(), c.getReplicationDegree());

        this.incrementReplicationObtained(c.getChunkInfo());
    }

    /**
     * Opposite of addChunk.
     * @param chunkInfo
     */
    public void removeChunk(ChunkInfo chunkInfo) {
        this.storedChunks.remove(chunkInfo);
        this.desiredReplication.remove(chunkInfo);

        this.decrementReplicationObtained(chunkInfo);
    }

    public void saveChunkToDisk(Chunk c){
        String pathFileId = CHUNKS_DIR + "/" + c.getFileId();
        String pathChunkNo = pathFileId + "/" + c.getChunkNo() + ".txt";

        File f = new File(pathFileId);

        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            f.mkdirs();
            System.out.println("Foi criado o ficheiro '" + pathChunkNo + "'." );
        }

        try {

            OutputStream os = new FileOutputStream(fChunk);

            System.out.println("Length of the chunk(bytes): " + c.getData().length);
            os.write(c.getData());

            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Peer.getDb().getDisk().saveFile(fChunk.length());
    }

    public long deleteChunkFromDisk(ChunkInfo info){

        File file = new File(CHUNKS_DIR + "/" + info.getFileId() + "/" + info.getChunkNo() + "txt");
        long fileLength = file.length();


        file.delete();

        Peer.getDb().getStoredChunksDb().removeChunk(info);
        Peer.getDb().getDisk().removeFile(fileLength);

        return fileLength;
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
