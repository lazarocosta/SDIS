package files;

import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StoredChunksDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String CHUNKS_DIR = "CHUNKS/peer" + Peer.getSenderId() + "/";

    private Map<ChunkInfo, byte[]> storedData;
    private Map<ChunkInfo, Integer> desiredReplication;
    private Map<ChunkInfo, Integer> obtainedReplication;

    StoredChunksDatabase() {
        this.storedData = new HashMap<>();
        this.desiredReplication = new HashMap<>();
        this.obtainedReplication = new HashMap<>();
    }

    /**
     * Adds a chunk to the volatile memory (hashmap) and also saves its current replication and desired replication.
     *
     * @param c Chunk
     */
    public void addChunk(Chunk c) {
        this.storedData.put(c.getChunkInfo(), c.getData());
        this.desiredReplication.put(c.getChunkInfo(), c.getReplicationDegree());

        this.incrementReplicationObtained(c.getChunkInfo());
    }

    /**
     * Opposite of addChunk.
     *
     * @param chunkInfo
     */
    public void removeChunk(ChunkInfo chunkInfo) {
        this.storedData.remove(chunkInfo);
        this.desiredReplication.remove(chunkInfo);

        this.decrementReplicationObtained(chunkInfo);
    }

    public void saveChunkToDisk(Chunk c) {
        String pathFileId = CHUNKS_DIR + "/" + c.getFileId();
        String pathChunkNo = pathFileId + "/" + c.getChunkNo() + ".txt";

        File f = new File(pathFileId);
        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            f.mkdirs();
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

    /**
     * Returns the size of the deleted chunk in BYTES
     *
     * @param info
     * @return
     */
    public long deleteChunkFromDisk(ChunkInfo info) {

        File file = new File(CHUNKS_DIR + "/" + info.getFileId() + "/" + info.getChunkNo() + ".txt");
        long fileLength = file.length();

        file.delete();
        System.out.println("delete diretory" + file.toPath());

        Peer.getDb().getStoredChunksDb().removeChunk(info);
        Peer.getDb().getDisk().removeFile(fileLength);

        return fileLength;
    }

    public void incrementReplicationObtained(ChunkInfo chunkInfo) {
        if (this.obtainedReplication.containsKey(chunkInfo)) {
            this.obtainedReplication.put(chunkInfo, this.obtainedReplication.get(chunkInfo) + 1);
        } else {
            this.obtainedReplication.put(chunkInfo, 1);
        }
    }

    public void decrementReplicationObtained(ChunkInfo chunkInfo) {

        if (this.obtainedReplication.containsKey(chunkInfo)) {
            this.obtainedReplication.put(chunkInfo, this.obtainedReplication.get(chunkInfo) - 1);
        } else {
            this.obtainedReplication.put(chunkInfo, -1);
        }

    }

    public void resetReplicationObtained(ChunkInfo chunkInfo) {
        this.obtainedReplication.put(chunkInfo, 0);
    }

    public Map<ChunkInfo, byte[]> getStoredData() {
        return storedData;
    }

    public boolean existsChunkInfo(ChunkInfo chunkInfo) {
        return storedData.containsKey(chunkInfo);
    }

    public byte[] getChunkData(ChunkInfo chunkInfo) {
        return storedData.get(chunkInfo);
    }

    public Map<ChunkInfo, Integer> getDesiredReplication() {
        return desiredReplication;
    }

    public Map<ChunkInfo, Integer> getObtainedReplication() {
        return obtainedReplication;
    }
}
