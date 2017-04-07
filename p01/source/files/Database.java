package files;

import chunk.Chunk;
import systems.Peer;

import java.io.*;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Database implements Serializable {


    private final String RESTORES_DIR = "RESTORES/peer" + Peer.getSenderId()+"/";
    private final String FILES_DIR = "FILES/";

    private BackedUpFilesDatabase backedUpFilesDb;
    private StoredChunksDatabase storedChunksDb;
    private Disk disk;

    public Database() {
        this.backedUpFilesDb = new BackedUpFilesDatabase();
        this.storedChunksDb = new StoredChunksDatabase();
        this.disk = new Disk();
    }

    // GETTERS


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


    public void removeBackup(String path) {

        //this.removeBackup(path);
    }

    public String getFileId(String path) { return this.backedUpFilesDb.getFileId(path); }


    // For testing
    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Database data = new Database();
        System.out.println("muda");

    }
}
