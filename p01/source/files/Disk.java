package files;

import java.io.Serializable;

/**
 *
 */
public class Disk implements Serializable{

    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_STORAGE_SPACE = 1024*1000; // 1024 kBytes

    private int storageSpace;
    private long usedBytes;


    public Disk() {
        this(DEFAULT_STORAGE_SPACE);
    }

    public Disk(int storageSpace)
    {
        this.storageSpace = storageSpace;
        this.usedBytes = 0;
    }

    public synchronized boolean saveFile(long fileByteSize){
        try{
            if(fileByteSize > this.getFreeBytes())
            {
                throw new Exception("Cannot save file with size larger than available space in disk .");
            }
            else
            {
                this.usedBytes = this.usedBytes + fileByteSize;
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    public synchronized boolean removeFile(long fileByteSize){
        try{
            if(fileByteSize > this.getFreeBytes())
            {
                throw new Exception("Cannot remove file with size larger than occupied space in disk.");
            }
            else
            {
                this.usedBytes = this.usedBytes - fileByteSize;
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public long getUsedBytes() {
        return usedBytes;
    }

    public long getFreeBytes(){
        return storageSpace - usedBytes;
    }

    public void setStorageSpace(int bytes)
    {
        this.storageSpace = bytes;
    }

    public void addStorageSpace(int bytes){
        this.storageSpace = this.storageSpace + bytes;
    }


    public void removeStorageSpace(int bytes){
        this.storageSpace = this.storageSpace - bytes;
    }

    @Override
    public String toString() {
        return "Disk: space = " + this.storageSpace + " bytes; used = " + this.usedBytes;
    }

    // FOR TESTING
    public static void main(String[] args) {
        Disk d1 = new Disk();
        Disk d2 = new Disk(1000);

        System.out.println(d1);
        System.out.println(d2);



        d2.saveFile(1000);
        System.out.println(d2);
        d2.saveFile(1);
        d2.removeFile(1002);

        d2.setStorageSpace(1002);
        d2.saveFile(1);
        System.out.println(d2);
    }
}