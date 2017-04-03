package files;

import chunk.Chunk;

import java.io.*;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Database implements Serializable {

    private static final int DEFAULT_STORAGE_SPACE = 1024 * 1000; // 1024 kBytes
    private final String SAVED_COPIES_DIRECTORY = "/tmp/";

    private BackedUpFilesDatabase backedUpFilesDb;
    private StoredChunksDatabase storedChunksDb;


    private int storageSpace = DEFAULT_STORAGE_SPACE;

    public Database() {
        this.backedUpFilesDb = new BackedUpFilesDatabase();
        this.storedChunksDb = new StoredChunksDatabase();
    }

    public int getStorageSpace() {

        return storageSpace;
    }

    public String getSavedCopiesDirectory() {

        return SAVED_COPIES_DIRECTORY;
    }

    public StoredChunksDatabase getStoredChunksDb() {
        return storedChunksDb;
    }

    /**
     * Execute when a client asks for a file to be backed up.
     */
    public void saveBackedUpFile(MyFile myFile) {
        this.backedUpFilesDb.addFile(myFile);
    }


    public void removeBackup(String path) {

        this.removeBackup(path);
    }

    public BackedUpFilesDatabase getBackedUpFilesDb() {
        return backedUpFilesDb;
    }

    public String getFileId(String path) { return this.backedUpFilesDb.getFileId(path); }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Database data = new Database();
        System.out.println("muda");
        System.out.println(data.getStorageSpace());

    }
}
