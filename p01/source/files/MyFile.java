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
import java.util.HashMap;

public class MyFile {

    private int MAX_SIZE_CHUNK = 200;  // 64 KByte

    private HashMap<Integer, Chunk> chunksFile; //chunkNo--->chunk
    private String fileId;
    private String filepath;
    private int replicationDegree;
    private File file;


    public MyFile(String filepath, int replicationDegree) {
        this.filepath = filepath;
        this.replicationDegree = replicationDegree;
        this.file = new File(this.filepath);

        chunksFile = new HashMap<>();
        this.generateFileId();
    }

    public MyFile() {
        chunksFile = new HashMap<>();
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
        return this.chunksFile.get(chunkNo);
    }

    public ArrayList<Chunk> getChunks() {

        ArrayList<Chunk> chunks = new ArrayList<>();

        for (int i = 1; i <= chunksFile.size(); i++) {
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

    public void saveCopy() {

        String pathSenderId = Peer.getDb().getSavedCopiesDirectory() + "sender" + Peer.getSenderId();
        String pathSaveDirectory = pathSenderId + "/" + this.file.getParent();

        File f = new File(pathSaveDirectory);

        if (!f.exists()) {
            f.mkdirs();
            System.out.println("Directory '" + pathSaveDirectory +"' created.");
        }

        try {

            OutputStream is = new FileOutputStream(pathSaveDirectory + "/" + this.file.getName());

            System.out.println("Saved file '" +  this.filepath +"' in '" + pathSaveDirectory + "'.");
            is.write(Files.readAllBytes(Paths.get(this.filepath)));

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void divideFileIntoChunks(){

        int currentByte = 0;
        int currentChunk = 1;

        try {
            InputStream is = new FileInputStream(this.filepath);
            int size = is.available();
            System.out.println("size file " + size);


            while (currentByte < size) {
                byte[] body = new byte[MAX_SIZE_CHUNK];
                is.read(body, 0, MAX_SIZE_CHUNK);

                Chunk c = new Chunk(fileId, currentChunk, this.replicationDegree, body);

                chunksFile.put(currentChunk, c);
                currentByte += MAX_SIZE_CHUNK;
                currentChunk++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
