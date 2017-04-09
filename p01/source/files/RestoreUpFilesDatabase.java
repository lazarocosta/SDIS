package files;

import chunk.ChunkInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestoreUpFilesDatabase {

    private static  int CONFIRMATION_ERROR_FILE = 1;
    private Map<String, Integer> confirmationFile; // fileId e chunks recebidos desse ficheiro
    private Map<ChunkInfo, byte[]> restoredChunkDd; // chunks restore recebidos para fazer restore do ficheiro
    private ArrayList<ChunkInfo> responseRestore; //respostas do chunk restore

    public RestoreUpFilesDatabase() {

        this.restoredChunkDd = new HashMap<>();
        this.responseRestore = new ArrayList<>();
        this.confirmationFile = new HashMap<>();
    }

    public Map<ChunkInfo, byte[]> getRestoredChunkDd() {
        return restoredChunkDd;
    }

    public ArrayList<ChunkInfo> getResponseRestore() {
        return responseRestore;
    }

    public Map<String, Integer> getConfirmationFile() {
        return confirmationFile;
    }

    public void resetConfirlamationFile(String fileId){
        confirmationFile.remove(fileId);
    }

    public synchronized  void addConfirmationErrorFile(String fileId){

        confirmationFile.put(fileId, CONFIRMATION_ERROR_FILE);
    }

    public synchronized void addConfirmationFile(String fileId) {

        if (confirmationFile.containsKey(fileId)) {
            int confirmation = confirmationFile.get(fileId);
            confirmation++;
            confirmationFile.put(fileId, confirmation);

        } else
            confirmationFile.put(fileId, 1);
    }
}
