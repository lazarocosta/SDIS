package files;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ChunkDatabase implements Serializable{

    private static final long serialVersionUID = 1L;

    Map<String, String> db;    // <ChunkInfo, Chunk>
    Map<String, String> files;  // <String, FileInfo>

    public ChunkDatabase(){
        db = new HashMap<>();
        files = new HashMap<>();
    }

    public synchronized boolean containsChunk(String chunkID) {
        return db.containsKey(chunkID);
    }

    public synchronized void addChunk(String chunkID, String chunkInfo) {
        try{
            if (!containsChunk(chunkID)) {
                db.put(chunkID, chunkInfo);

                //TODO: Peer.saveDatabase();
            }
            else
            {
                throw new Exception("Could not add chunk. Database already contains a similiar chunk.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public synchronized void removeChunk(String chunkID) {
        db.remove(chunkID);

        // TODO: Peer.saveDatabase();
    }

    @Override
    public String toString() {
        return "Chunk database:\n" +  db.toString() + "\n" + "Files:\n" + files.toString() +"\n";
    }

    // Testing
    public static void main(String[] args) {

    }
}

