package files;

import java.io.*;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database implements Serializable {
    private static final int DEFAULT_STORAGE_SPACE = 1024 * 1000; // 1024 kBytes

    private HashMap<String, String> backedUpFiles; // key = path, value = fileId

    private HashMap<String, MyFile> files; // key = fileId, value = file


    private int storageSpace = DEFAULT_STORAGE_SPACE;
    private String path = "Database.txt";

    public int getStorageSpace() {
        return storageSpace;
    }

    public Database() {
        this.backedUpFiles = new HashMap<>();
        this.files = new HashMap<>();
    }
    public String getFileId(String path){
        return this.backedUpFiles.get(path);
    }

    /*
    apenas servidor iniciador executa isto
    */
    public void addFileBackup(String path) throws IOException, NoSuchAlgorithmException {
        MyFile myfile = new MyFile();
        myfile.generateFileId(path);

    }

    /*
        apenas servidor que recebe um chunk executa isto
     */
    public Boolean addFileToDatabase(String fileId, int replicationDegree, int chunkNo, int size) throws IOException, NoSuchAlgorithmException {

        if (!this.files.containsKey(fileId)) {

            if (storageSpace < size) return false;

            MyFile myFile = new MyFile();
            Chunk chunk = new Chunk(fileId, chunkNo, replicationDegree, null);
            myFile.addChunk(chunkNo, chunk);
            this.files.put(fileId, myFile);
            storageSpace -= size;

            return true;

        } else {
            MyFile myfile = this.files.get(fileId);
            Chunk chunk = new Chunk(fileId, chunkNo, replicationDegree, null);

            if (!myfile.exists(chunkNo)) {
                if (storageSpace < size) {
                    myfile.addChunk(chunkNo, chunk);
                    storageSpace -= size;
                    return true;
                }
            }
            return false;
        }
    }

    public boolean removeFileFromDatabase(String fileId) {
        if (this.files.remove(fileId) == null) {
            System.out.println("Could not remove file '" + fileId + "' from database, because it didn't exist.");
            return false;
        } else {
            return true;
        }
    }

    public boolean removeBackup(String path) {
        if (this.backedUpFiles.remove(path) == null) {
            System.out.println("Could not remove file '" + path + "' from backedUpFiles, because it didn't exist.");
            return false;
        } else {

            return true;
        }
    }

    public MyFile getFile(int index){
        return this.files.get(index);
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Database data = new Database();
        System.out.println("muda");
        System.out.println(data.getStorageSpace());

    }
}
