package files;

import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;

import java.io.*;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String RESTORES_DIR = "RESTORES/peer" + Peer.getSenderId() + "/";
    public static String DATA_FILE = "./DATA/peer" +  Peer.getSenderId() + "/database.data";


    private BackedUpFilesDatabase backedUpFilesDb;
    private StoredChunksDatabase storedChunksDb;
    private RestoreUpFilesDatabase restoreUpFilesDb;


    private Disk disk;

    public Database() {
        this.backedUpFilesDb = new BackedUpFilesDatabase();
        this.storedChunksDb = new StoredChunksDatabase();
        this.restoreUpFilesDb = new RestoreUpFilesDatabase();

        this.disk = new Disk();
    }

    // GETTERS


    public RestoreUpFilesDatabase getRestoreUpFilesDb() {
        return restoreUpFilesDb;
    }

    public Disk getDisk() {
        return disk;
    }

    public BackedUpFilesDatabase getBackedUpFilesDb() {
        return backedUpFilesDb;
    }

    public StoredChunksDatabase getStoredChunksDb() {
        return storedChunksDb;
    }

    // BACKED UP FILES FUNCTIONS

    /**
     * Execute when a client asks for a file to be backed up.
     */
    public void saveBackedUpFile(MyFile myFile) {
        this.backedUpFilesDb.addFile(myFile);
    }

    public String getFileId(String path) {
        return this.backedUpFilesDb.getFileId(path);
    }


    // For testing
    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Database data = new Database();
        System.out.println("muda");

    }
}
