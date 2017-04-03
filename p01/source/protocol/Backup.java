package protocol;

import chunk.Chunk;
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

    public static MyFile saveFileToBackedUpFiles(String filepath, int replicationDegree){

        MyFile myFile = new MyFile(filepath, replicationDegree);
        myFile.divideFileIntoChunks();  // create chunks and store them
        Peer.getDb().saveBackedUpFile(myFile);    // TODO: ask whether the PATH or the FILENAME should be saved

      //  System.out.println(Peer.getDb().getBackedUpFiles());
        System.out.println("aqui");

        //myFile.saveCopy();

        return myFile;
    }

    public static void sendBackupRequest(ArrayList<Chunk> chunks){

        for(Chunk c: chunks)
        {
            String message = Peer.getUdpChannelGroup().getMDB().messagePutChunk(Peer.getSenderId(),c);
            System.out.println(c.getFileId() + ";" + c.getChunkNo() + ";" + c.getReplicationDegree() + ";" + c.getData());
            Peer.getUdpChannelGroup().getMDB().sendsMessage(message);
            System.out.println("Sent to backup chunk: " + c.toString());
        }

    }

    public static void storeChunk(Chunk c)
    {
        Peer.getDb().getStoredChunksDb().addChunk(c);
    }

    public static void sendStoredMessage(Chunk c){

        Peer.getUdpChannelGroup().sendForControl(Peer.getUdpChannelGroup().getMC().messageStored(Peer.getSenderId(), c.getFileId(), c.getChunkNo()));

    }

    public static void handleBackupRequest(Message msg){

        if (msg.getSenderId() != Peer.getSenderId()) {

            Chunk c = new Chunk(msg.getFileId(), msg.getChunkNo(), msg.getReplicationDeg(), msg.getBody());
            Backup.storeChunk(c);

            sendStoredMessage(c);
        }


    }

    public static void saveChunk(String version, String fileId, int chunkNo, byte[] body) {

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



}
