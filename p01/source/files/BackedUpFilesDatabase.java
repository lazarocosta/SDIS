package files;

import chunk.ChunkInfo;
import systems.Peer;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class BackedUpFilesDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    private ConcurrentHashMap<String, ChunkInfo> pathToChunkInfo; // key = path, value = fileId --> Numero de chunks do fichiro no chunkInfo
    private ConcurrentHashMap<String, MyFile> pathToMyFile; // file name to MyFile

    BackedUpFilesDatabase() {
        this.pathToChunkInfo = new ConcurrentHashMap<>();
        this.pathToMyFile = new ConcurrentHashMap<>();
    }

    public boolean containsFileId(String fileId) {
        return (this.fileIdToFilePath(fileId) != null);
    }

    public void addFile(String path, ChunkInfo chunkInfo) {
        this.pathToChunkInfo.put(path, chunkInfo);
    }

    public void addFile(MyFile myFile) {
        ChunkInfo chunkInfo = new ChunkInfo(myFile.getFileId(), myFile.getChunksNum()); //--> numero total de chunks do file
        this.addFile(myFile.getFilename(), chunkInfo);

        this.pathToMyFile.put(myFile.getFilename(), myFile);

        Peer.saveDatabase();
    }

    public void removeFileByPath(String path) {

        if (this.pathToChunkInfo.containsKey(path)) {
            this.pathToChunkInfo.remove(path);
            System.out.println("Deleted file with name: " + path);
        }

        this.pathToMyFile.remove(path);

        Peer.saveDatabase();
    }

    public String getFileId(String path) {
        ChunkInfo chunkInfo = pathToChunkInfo.get(path);
        return chunkInfo.getFileId();
    }

    public ChunkInfo getChunkInfo(String path) {
        return this.pathToChunkInfo.get(path);
    }

    public boolean containsPath(String path) {
        return pathToChunkInfo.containsKey(path);
    }

    public String fileIdToFilePath(String fileId) {

        for (String key : this.pathToChunkInfo.keySet()) {
            ChunkInfo chunkInfo = pathToChunkInfo.get(key);
            if (chunkInfo.getFileId().equals(fileId))
                return key;
        }

        return null;
    }

    public ConcurrentHashMap<String, MyFile> getPathToMyFile() {
        return pathToMyFile;
    }
}

