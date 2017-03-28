package files;

import sun.rmi.runtime.Log;

import java.io.Serializable;

/**
 *
 */
public class Disk implements Serializable {

    private static final int DEFAULT_STORAGE_SPACE = 1024 * 1000; // 1024 kBytes

    private String filename;
    private int storageSpace;
    private int usedBytes;

    public Disk() {
        this("disk", DEFAULT_STORAGE_SPACE);
    }

    public Disk(String filename, int storageSpace) {
        this.filename = filename;
        this.storageSpace = storageSpace;
        this.usedBytes = 0;
    }

    public synchronized boolean saveFile(int fileByteSize) {
        if (fileByteSize <= this.getFreeBytes()) {
            this.addStorageSpace(fileByteSize);
            return true;
        } else return false;

    }

    public synchronized void removeFile(int fileByteSize) {

        this.removeStorageSpace(fileByteSize);
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public int getUsedBytes() {
        return usedBytes;
    }

    public int getFreeBytes() {
        return storageSpace - usedBytes;
    }

    public void setStorageSpace(int bytes) {
        this.storageSpace = bytes;
    }

    public void addStorageSpace(int bytes) {
        this.storageSpace = this.storageSpace + bytes;
    }

    public void removeStorageSpace(int bytes) {
        this.storageSpace = this.storageSpace - bytes;
    }

    @Override
    public String toString() {
        return "Disk: space = " + this.storageSpace + " bytes; used = " + this.usedBytes + " filename = " + this.filename;
    }

    // FOR TESTING
    public static void main(String[] args) {
        Disk d1 = new Disk();
        Disk d2 = new Disk("hey.a", 1000);

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
