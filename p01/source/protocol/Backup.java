package protocol;

import files.Chunk;
import files.MyFile;
import systems.Peer;

import java.util.ArrayList;

/**
 * Created by jazz on 29-03-2017.
 */
public class Backup extends SubProtocol{

    public static MyFile saveFileToBackedUpFiles(String filepath, int replicationDegree){

        MyFile myFile = new MyFile(filepath, replicationDegree);
        myFile.divideFileIntoChunks();  // create chunks and store them
        myFile.saveCopy();

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

}
