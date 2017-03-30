package files;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MyFile {

    private int MAX_SIZE = 2;  // 64 KByte
    private int replication;

    public MyFile(int replication) {
        this.replication = replication;

    }

    public ArrayList<Chunk> divideFileIntoChunks(File path, String fileId) throws IOException {

        int currentByte = 0;
        int currentChunk = 1;

        ArrayList<Chunk> fileChunks = new ArrayList<>();

        InputStream is = new FileInputStream(path);
        int size = is.available();
        System.out.println("size file " + size);


        while (currentByte < size) {
            byte[] body = new byte[MAX_SIZE];
            is.read(body, 0, MAX_SIZE);

            Chunk c = new Chunk(fileId, currentChunk, replication, body);
            fileChunks.add(c);

            currentByte += MAX_SIZE;
            currentChunk++;
        }
        return fileChunks;
    }

    public String generateFileId(File file) throws NoSuchAlgorithmException, IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        String str = file.getName() + attr.creationTime() + attr.size();    // generate hash from file name, creation time and size
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();

        String encodedFileId = DatatypeConverter.printHexBinary(digest).toLowerCase();
        return encodedFileId;
    }
}
