package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import files.MyFile;
import systems.Peer;
import utils.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jazz on 29-03-2017.
 */
public class Reclaim extends SubProtocol{

    public static void reclaimInitiator(int size_in_kb){

        System.out.println("Peer is reclaiming " + size_in_kb + "kB of Disk.");

        Map<ChunkInfo, Integer> orderedReplications = MapUtil.sortByValue(Peer.getDb().getStoredChunksDb().getObtainedReplication());



    }

    private static Map<ChunkInfo, Integer> getDifferenceBetweenDesiredAndObtainedReplication (){

        Map<ChunkInfo, Integer> result = new HashMap<>();

        for (Map.Entry<ChunkInfo, Integer> entry : Peer.getDb().getStoredChunksDb().getObtainedReplication().entrySet())
        {
            ChunkInfo info =  entry.getKey();

            // TODO: continue

            //Chunk c = Peer.getDb().getStoredChunksDb().getStoredChunks().get(info);
        }

        return result;
    }

    private static ArrayList<ChunkInfo> getMostReplicatedChunks(){
        ArrayList<ChunkInfo> result = new ArrayList<>();

        // TODO CONTINUE

        return result;
    }
}
