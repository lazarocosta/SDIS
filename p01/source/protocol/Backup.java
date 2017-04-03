package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import files.MyFile;
import systems.Peer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by jazz on 29-03-2017.
 */
public class Backup extends SubProtocol{

    public static void backupInitiator(String path, int replicationDegree){
        System.out.println("Peer is executing backup of file '" + path + "' with replication degree " + replicationDegree);

        MyFile myFile = Backup.saveFileToBackedUpFiles(path, replicationDegree);    // create and save file in initiation server
        System.out.println("Created myFile.");

        sendBackupRequest(myFile.getChunks());
        System.out.println("Sent PUTCHUNK requests.");
    }

    public static void backupHandler(Message msg){

        if (!Peer.getDb().getBackedUpFilesDb().containsFileId(msg.getFileId())) {
            Chunk c = new Chunk(msg.getFileId(), msg.getChunkNo(), msg.getReplicationDeg(), msg.getBody());
            saveChunk(msg.getVersion(),msg.getFileId(),msg.getChunkNo(), msg.getBody());
            storedInitiator(c);
        }
        else
        {
            System.out.println("This server was the initiator in the backup of '" +  msg.getFileId() + "'.");
        }

    }

    public static void storedInitiator(Chunk c){

        if(canPeerStoreChunk(c)) {
            storeChunk(c);
            sendStoredMessage(c);
        }

    }

    public static void storedHandler(Message msg){

        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        Peer.getDb().getStoredChunksDb().incrementReplicationObtained(chunkInfo);

        String filepath;
        if((filepath = Peer.getDb().getBackedUpFilesDb().fileIdToFilePath(msg.getFileId())) != null){
            System.out.println("Chunk from " +  filepath + " with number " + msg.getChunkNo() + " has been saved by server with id " + msg.getSenderId());
        }
    }


    private static MyFile saveFileToBackedUpFiles(String filepath, int replicationDegree){

        MyFile myFile = new MyFile(filepath, replicationDegree);
        myFile.divideFileIntoChunks();  // create chunks and store them
        Peer.getDb().saveBackedUpFile(myFile);    // TODO: ask whether the PATH or the FILENAME should be saved

      //  System.out.println(Peer.getDb().getBackedUpFiles());
        System.out.println("aqui");

        //myFile.saveCopy();

        return myFile;
    }

    private static void sendBackupRequest(ArrayList<Chunk> chunks){

        for(Chunk c: chunks)
        {
            String message = Peer.getUdpChannelGroup().getMDB().messagePutChunk(Peer.getSenderId(),c);
            System.out.println(c.getFileId() + ";" + c.getChunkNo() + ";" + c.getReplicationDegree() + ";" + c.getData());
            Peer.getUdpChannelGroup().getMDB().sendsMessage(message);
            System.out.println("Sent to backup chunk: " + c.toString());
        }

    }

    private static void storeChunk(Chunk c) {
        Peer.getDb().getStoredChunksDb().addChunk(c);
    }

    private static void sendStoredMessage(Chunk c){

        Peer.getUdpChannelGroup().sendForControl(Peer.getUdpChannelGroup().getMC().messageStored(Peer.getSenderId(), c.getFileId(), c.getChunkNo()));

    }

    private static void saveChunk(String version, String fileId, int chunkNo, byte[] body) {

        System.out.println("esttetete");
        String pathSenderId = "sender" + Peer.getSenderId();
        String pathFileId = pathSenderId + "/" + fileId;
        String pathChunkNo = pathFileId + "/" + chunkNo + ".txt";

        File f = new File(pathFileId);

        File fChunk = new File(pathChunkNo);

        if (!f.exists()) {
            f.mkdirs();
            System.out.println("criou path ");
        }

        try {

            OutputStream is = new FileOutputStream(fChunk);

            System.out.println("length " + body.length);
            is.write(body);

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static boolean canPeerStoreChunk(Chunk c)
    {
        return (Peer.getDb().getBackedUpFilesDb().fileIdToFilePath(c.getFileId()) == null);
    }


}
