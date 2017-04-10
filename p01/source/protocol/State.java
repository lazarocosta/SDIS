package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import files.MyFile;
import systems.Peer;

import java.util.Map;

/**
 * Created by jazz on 09-04-2017.
 */
public class State {

    public static void show(){

        // Iterate through backed up files
        if(Peer.getDb().getBackedUpFilesDb().getPathToMyFile() == null || Peer.getDb().getBackedUpFilesDb().getPathToMyFile().size() == 0) {
            System.out.println("\nBACKED UP FILES: \n");

            for (Map.Entry<String, MyFile> entry : Peer.getDb().getBackedUpFilesDb().getPathToMyFile().entrySet()) {
                MyFile currentFile = entry.getValue();

                System.out.println("Path: " + currentFile.getFilepath());
                System.out.println("FileID: " + currentFile.getFileId());
                System.out.println("Desired replication degree: " + currentFile.getReplicationDegree());
                System.out.println("Chunks of the file: ");

                for (Chunk c : currentFile.getChunks()) {
                    System.out.println("\t" + c.getChunkInfo());
                    System.out.println("\tObtained replication: " + Peer.getDb().getStoredChunksDb().getObtainedReplication().get(c.getChunkInfo()));
                    System.out.println();
                }

                System.out.println();
            }
        }

        else{
            System.out.println("\nNo backed up files yet.");
        }




        if(Peer.getDb().getStoredChunksDb().getStoredData().entrySet().size() != 0) {

            System.out.println("\nChunks stored:\n");

            for (Map.Entry<ChunkInfo, byte[]> entry : Peer.getDb().getStoredChunksDb().getStoredData().entrySet()) {
                System.out.println("Info: " + entry.getKey());
                System.out.println("Size: " + entry.getValue().length / 1000 + " kB");
                System.out.println("\tObtained replication: " + Peer.getDb().getStoredChunksDb().getObtainedReplication().get(entry.getKey()));
                System.out.println();
            }
        }

        else{
            System.out.println("\nNo chunks stored yet.\n");
        }

        System.out.println("Disk capacity: " + Peer.getDb().getDisk().getStorageSpace()/ 1000 + " kB; occupied: " + Peer.getDb().getDisk().getUsedBytes()/1000 + " kB.");
    }
}
