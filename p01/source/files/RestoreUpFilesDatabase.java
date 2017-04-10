package files;

import chunk.ChunkInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestoreUpFilesDatabase implements Serializable{

    private static final long serialVersionUID = 1L;

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

    public synchronized void addConfirmationFile(String fileId) {

        if (confirmationFile.containsKey(fileId)) {
            int confirmation = confirmationFile.get(fileId);
            confirmation++;
            confirmationFile.put(fileId, confirmation);

        } else
            confirmationFile.put(fileId, 1);
    }


}