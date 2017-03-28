package files;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;

import static java.util.Arrays.copyOfRange;


public class MyFile {

    public static ArrayList<Chunk> divideFileIntoChunks(File file, Map<String, String> fileIdMap){

        ArrayList<Chunk> fileChunks = new ArrayList<Chunk>();

        try{

            int currentByte = 0;
            int currentChunk = 1;

            byte[] data = Files.readAllBytes(file.toPath());

            String fileId = null;

            try {
                fileId = generateFileId(file);
                fileIdMap.put(fileId, file.getName());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            while(currentByte < data.length)
            {
                byte[] dataChunk = copyOfRange(data, currentByte, Chunk.MAX_SIZE);
                Chunk c = new Chunk(fileId, currentChunk, 1, dataChunk);
                fileChunks.add(c);

                currentByte += Chunk.MAX_SIZE;
                currentChunk++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return fileChunks;
    }

    public static String generateFileId(File file) throws NoSuchAlgorithmException, IOException {

        BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);

        String str = file.getName() + attr.creationTime() + attr.size();    // generate hash from file name, creation time and size

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();

        String encodedFileId = DatatypeConverter.printHexBinary(digest).toLowerCase();

        return encodedFileId;

    }
}
