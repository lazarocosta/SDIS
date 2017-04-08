package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import files.MyFile;
import systems.Peer;
import utils.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Reclaim extends SubProtocol{

    public static void reclaimInitiator(int size_in_kb){

        System.out.println("Peer is reclaiming " + size_in_kb + "kB of Disk.");




    }

    private static Map<ChunkInfo, Integer> getDifferenceBetweenDesiredAndObtainedReplication (){

        Map<ChunkInfo, Integer> result = new HashMap<>();

        for (Map.Entry<ChunkInfo, byte[]> entry : Peer.getDb().getStoredChunksDb().getStoredChunks().entrySet())
        {
            ChunkInfo info =  entry.getKey();

            int desiredReplication = Peer.getDb().getStoredChunksDb().getDesiredReplication().get(info);
            int obtainedReplication = Peer.getDb().getStoredChunksDb().getObtainedReplication().get(info);

            result.put(info, obtainedReplication - desiredReplication);
        }

        return result;
    }

    private static ArrayList<ChunkInfo> getMostReplicatedChunks(Map<ChunkInfo, Integer> deltaReplication){

        Map<ChunkInfo, Integer> orderedByDeltaReplication = MapUtil.sortByValue(deltaReplication);

        ArrayList<ChunkInfo> result = new ArrayList<>();

        for (Map.Entry<ChunkInfo, Integer> entry : orderedByDeltaReplication.entrySet()) {
            result.add(entry.getKey());
        }

        return result;
    }

    private static void deleteChunks (int spaceToReclaim, ArrayList<ChunkInfo> mostReplicatedChunks){

        int spaceFreed = 0;

        while(spaceFreed < spaceToReclaim){

            // TODO: VERIFY IF CHUNK CAN BE DELETED

            spaceFreed += Peer.getDb().getStoredChunksDb().deleteChunkFromDisk(mostReplicatedChunks.get(0));

        }

    }

    // FOR TESTING
    public static void main(String[] args) {

    }
}
