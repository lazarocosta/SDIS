package files;

import systems.Peer;

import java.io.*;

public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

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
    public synchronized RestoreUpFilesDatabase getRestoreUpFilesDb() {
        return restoreUpFilesDb;
    }

    public synchronized Disk getDisk() {
        return disk;
    }

    public synchronized BackedUpFilesDatabase getBackedUpFilesDb() {
        return backedUpFilesDb;
    }

    public synchronized StoredChunksDatabase getStoredChunksDb() {
        return storedChunksDb;
    }

    // BACKED UP FILES FUNCTIONS

    /**
     * Execute when a client asks for a file to be backed up.
     */
    public synchronized void saveBackedUpFile(MyFile myFile) {
        this.backedUpFilesDb.addFile(myFile);
    }

    public synchronized String getFileId(String path) {
        return this.backedUpFilesDb.getFileId(path);
    }
}
