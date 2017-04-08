package files;


import chunk.Chunk;
import chunk.ChunkInfo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BackedUpFilesDatabase implements Serializable {

    private final String DATABASE_FILE = "backup.data";
    private HashMap<String, ChunkInfo> pathToFileIdMap; // key = path, value = fileId --> Numero de chunks do fichiro no chunkInfo

    BackedUpFilesDatabase() {
        this.pathToFileIdMap = new HashMap<>();
    }

    public void saveDatabase() {
        File file = new File(this.DATABASE_FILE);
        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(this.pathToFileIdMap);
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDatabase() {
        File file = new File(this.DATABASE_FILE);
        if (file.exists()) {
            try {
                FileInputStream f = new FileInputStream(file);
                ObjectInputStream s = new ObjectInputStream(f);
                this.pathToFileIdMap = (HashMap<String, ChunkInfo>) s.readObject();

                System.out.println("load Database");
                s.close();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean containsFileId(String fileId) {
        return (this.fileIdToFilePath(fileId) != null);
    }

    public void addFile(String path, ChunkInfo chunkInfo) {

        this.pathToFileIdMap.put(path, chunkInfo);

        this.saveDatabase();
    }

    public void addFile(MyFile myFile) {
        ChunkInfo chunkInfo = new ChunkInfo(myFile.getFileId(), myFile.getChunksNum()); //--> numero total de chunks do file
        this.addFile(myFile.getFilepath(), chunkInfo);
    }

    public void removeFileByPath(String path) {

        System.out.println(pathToFileIdMap);

        if (this.pathToFileIdMap.containsKey(path)) {
            this.pathToFileIdMap.remove(path);
            this.saveDatabase();
            System.out.println("delete file in to <<pathToFileIdMap>>");
        }
    }

    public String getFileId(String path) {
        ChunkInfo chunkInfo = pathToFileIdMap.get(path);
        return chunkInfo.getFileId();
    }

    public ChunkInfo getChunkInfo(String path) {
        return this.pathToFileIdMap.get(path);
    }

    public boolean containsPath(String path) {
        return pathToFileIdMap.containsKey(path);
    }


    public String fileIdToFilePath(String fileId) {


        for (String key : this.pathToFileIdMap.keySet()) {
            ChunkInfo chunkInfo = pathToFileIdMap.get(key);
            if (chunkInfo.getFileId().equals(fileId))
                return key;
        }


        return null;
    }
}

