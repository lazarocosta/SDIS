package files;

import chunk.ChunkInfo;

import java.io.*;
import java.util.HashMap;

public class BackedUpFilesDatabase implements Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String, ChunkInfo> pathToChunkInfo; // key = path, value = fileId --> Numero de chunks do fichiro no chunkInfo
    private HashMap<String, MyFile> pathToMyFile;

    BackedUpFilesDatabase() {
        this.pathToChunkInfo = new HashMap<>();
        this.pathToMyFile = new HashMap<>();
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
    }

    public void removeFileByPath(String path) {

        System.out.println(pathToChunkInfo);

        if (this.pathToChunkInfo.containsKey(path)) {
            this.pathToChunkInfo.remove(path);
            System.out.println("delete file in to <<pathToChunkInfo>>");
        }

        this.pathToMyFile.remove(path);
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

    public HashMap<String, MyFile> getPathToMyFile() {
        return pathToMyFile;
    }
}

