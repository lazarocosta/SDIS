package files;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 */
public class FileDatabase {

    private Map<String, String> backedUpFiles; // key = filename, value = fileId

    public FileDatabase() {
        this.backedUpFiles = new HashMap<>();
    }

    public Map<String, String> getBackedUpFiles() {
        return backedUpFiles;
    }

    /**
     * Returns true if the server can store chunks of a file,
     * i.e. if the server was not the initiator-peer when backing up the file.
     * @param fileId File fileId.
     * @return true or false
     */
    public boolean canStoreChunksOfFile(String fileId)
    {
        if(this.backedUpFiles.containsValue(fileId))
            return false;
        else
            return true;
    }

    public boolean containsFileId(String fileId)
    {
        if(this.backedUpFiles.containsValue(fileId))
            return false;
        else
            return true;
    }


    public void addFileToDatabase(String filename, String fileId){

        // TODO: Ask what to do if filename already exists in database
        if(this.backedUpFiles.containsKey(filename))
        {
            this.backedUpFiles.remove(filename);
        }

        this.backedUpFiles.put(filename, fileId);
    }

    /**
     *
     * @param filename file to remove
     * @return true if file has been removed, false if filename didn't exists in the first place
     */
    public boolean removeFileFromDatabase(String filename)
    {
        if(this.backedUpFiles.remove(filename) == null)
        {
            System.out.println("Could not remove file '" + filename + "' from database, because it didn't exist.");
            return false;
        }
        else
        {
            return true;
        }
    }
}
