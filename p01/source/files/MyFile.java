package files;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MyFile {

    private int MAX_SIZE_CHUNK = 200;  // 64 KByte
    private HashMap<Integer, Chunk> chunksFile; //chunckNo--->chunk
    private String fileId;


    public MyFile() {
        chunksFile = new HashMap<>();

    }

    public String getFileId() {
        return fileId;
    }

    public Chunk getChunk(int chuncNo) {
        return this.chunksFile.get(chuncNo);
    }

    public ArrayList<Chunk> getChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();

        for (int i = 0; i < chunksFile.size(); i++) {
            chunks.add(chunksFile.get(i));
        }
        return chunks;
    }
    public boolean exists(int chunkNo) {
        if (!this.chunksFile.containsKey(chunkNo)) return true;
        else return false;
    }

    public void addChunk(int chunkNo, Chunk chunk) {

        chunksFile.put(chunkNo, chunk);
    }

    public String generateFileId(String path) throws NoSuchAlgorithmException, IOException {

        File file = new File(path);

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        String str = file.getName() + attr.creationTime() + attr.size();    // generate hash from file name, creation time and size
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();

        fileId = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return fileId;
    }

    public void divideFileIntoChunks(File path, int replication) throws IOException {

        int currentByte = 0;
        int currentChunk = 1;

        InputStream is = new FileInputStream(path);
        int size = is.available();
        System.out.println("size file " + size);


        while (currentByte < size) {
            byte[] body = new byte[MAX_SIZE_CHUNK];
            is.read(body, 0, MAX_SIZE_CHUNK);

            Chunk c = new Chunk(fileId, currentChunk, replication, body);

            chunksFile.put(currentChunk, c);
            currentByte += MAX_SIZE_CHUNK;
            currentChunk++;
        }
    }
}
