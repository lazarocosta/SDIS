package files;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BackedUpFilesDatabase implements Serializable {

    private final String DATABASE_FILE = "backup.data";
    private HashMap<String, String> pathToFileIdMap; // key = path, value = fileId --> Ficheiros que este servidor pediu para guardar

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
        try {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            this.pathToFileIdMap =(HashMap<String, String> ) s.readObject();

            System.out.println("load Database");
            s.close();
        } catch (ClassNotFoundException| IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canSaveChunksOfFile(String filepath) {
        return (!this.pathToFileIdMap.containsKey(filepath));
    }
    public boolean containsFileId(String fileId) {
        return (this.fileIdToFilePath(fileId) != null);
    }

    public void addFile(String path, String fileId) {
        this.pathToFileIdMap.put(path, fileId);

        this.saveDatabase();
    }

    public void addFile(MyFile myFile) {
        this.addFile(myFile.getFilepath(), myFile.getFileId());
    }

    public void removeFileByPath(String path) {

        if (this.pathToFileIdMap.containsKey(path)) {
            this.pathToFileIdMap.remove(path);
            this.saveDatabase();
            System.out.println("delete file in to <<pathToFileIdMap>>");
        }
    }

    public String getFileId(String path){
        return pathToFileIdMap.get(path);
    }

    public boolean containsKey(String key){
        return  pathToFileIdMap.containsKey(key);
    }


    public String fileIdToFilePath(String fileId) {
        for (String key : this.pathToFileIdMap.keySet()) {
            if (this.pathToFileIdMap.get(key).equals(fileId)) {
                return key;
            }
        }
        return null;
    }
}

