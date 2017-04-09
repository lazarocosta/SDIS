package files;

import chunk.Chunk;
import systems.Peer;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MyFile implements Serializable{

    private static final long serialVersionUID = 1L;

    private HashMap<Integer, Chunk> fileChunks; //chunkNo--->chunk
    private String fileId;
    private String filepath;
    private int replicationDegree;
    private File file;


    public MyFile(String filepath, int replicationDegree) {
        this.filepath = filepath;
        this.replicationDegree = replicationDegree;
        this.file = new File(this.filepath);

        fileChunks = new HashMap<>();
        this.generateFileId();
    }

    public MyFile() {
        fileChunks = new HashMap<>();
    }

    public File getFile() {
        return file;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getFileId() {
        return fileId;
    }

    public Chunk getChunk(int chunkNo) {
        return this.fileChunks.get(chunkNo);
    }

    public ArrayList<Chunk> getChunks() {

        ArrayList<Chunk> chunks = new ArrayList<>();

        for (int i = 1; i <= fileChunks.size(); i++) {
            chunks.add(fileChunks.get(i));
        }

        return chunks;
    }

    public int getChunksNum() {
        return fileChunks.size();
    }

    public boolean exists(int chunkNo) {
        if (!this.fileChunks.containsKey(chunkNo)) return true;
        else return false;
    }

    public void addChunk(int chunkNo, Chunk chunk) {

        fileChunks.put(chunkNo, chunk);
    }


    public void generateFileId() {

        File file = new File(this.filepath);

        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            String str = file.getName() + attr.creationTime() + attr.size();    // generate hash from file name, creation time and size
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            byte[] digest = md.digest();

            fileId = DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void divideFileIntoChunks() {

        System.out.println("Dividing chunks.");

        int currentByte = 0;
        int currentChunk = 1;

        try {

            InputStream is = new FileInputStream(this.filepath);
            int size = is.available();
            System.out.println("File size is " + size + " bytes.");

            while (currentByte <= size) {

                byte[] body;
                int bytesLeft = size - currentByte;


                if (bytesLeft < Chunk.MAX_SIZE) {

                    body = new byte[bytesLeft];

                    is.read(body, 0, bytesLeft);
                } else {

                    body = new byte[Chunk.MAX_SIZE];
                    is.read(body, 0, Chunk.MAX_SIZE);
                }

                Chunk c = new Chunk(fileId, currentChunk, this.replicationDegree, body);


                fileChunks.put(currentChunk, c);
                currentByte += Chunk.MAX_SIZE;
                currentChunk++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
