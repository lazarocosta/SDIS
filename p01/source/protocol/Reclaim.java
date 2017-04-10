package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;
import utils.MapUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Reclaim extends SubProtocol {

    public static void reclaimInitiator(int size_in_kb) {

        System.out.println("Peer is reclaiming " + size_in_kb + "kB of Disk.");

        Map<ChunkInfo, Integer> differenceBetweenDesiredAndObtainedReplication = getDifferenceBetweenDesiredAndObtainedReplication();
        ArrayList<ChunkInfo> mostReplicatedChunks = getMostReplicatedChunks(differenceBetweenDesiredAndObtainedReplication);
        deleteChunks(size_in_kb, mostReplicatedChunks);

    }

    private static Map<ChunkInfo, Integer> getDifferenceBetweenDesiredAndObtainedReplication() {

        Map<ChunkInfo, Integer> result = new HashMap<>();

        for (Map.Entry<ChunkInfo, byte[]> entry : Peer.getDb().getStoredChunksDb().getStoredData().entrySet()) {
            ChunkInfo info = entry.getKey();

            int desiredReplication = Peer.getDb().getStoredChunksDb().getDesiredReplication().get(info);
            int obtainedReplication = Peer.getDb().getStoredChunksDb().getObtainedReplication().get(info);

            result.put(info, obtainedReplication - desiredReplication);
        }

        return result;
    }

    private static ArrayList<ChunkInfo> getMostReplicatedChunks(Map<ChunkInfo, Integer> deltaReplication) {

        Map<ChunkInfo, Integer> orderedByDeltaReplication = MapUtil.sortByValue(deltaReplication);

        ArrayList<ChunkInfo> result = new ArrayList<>();

        for (Map.Entry<ChunkInfo, Integer> entry : orderedByDeltaReplication.entrySet()) {
            result.add(entry.getKey());
        }

        return result;
    }

    private static int deleteChunks(int spaceToReclaim, ArrayList<ChunkInfo> mostReplicatedChunks) {

        int spaceFreed = 0;
        int i = 0;

        while ((spaceFreed < spaceToReclaim) && (i < mostReplicatedChunks.size())) {

            ChunkInfo info = mostReplicatedChunks.get(i);

            byte[] currentChunkData = Peer.getDb().getStoredChunksDb().getStoredData().get(mostReplicatedChunks.get(i));

            Chunk currentChunk = new Chunk(info.getFileId(), info.getChunkNo(), Peer.getDb().getStoredChunksDb().getDesiredReplication().get(info), currentChunkData);

            Backup.VerifyStoredConfirms verifyStoredConfirms = new Backup.VerifyStoredConfirms(currentChunk);
            verifyStoredConfirms.run();

            if (verifyStoredConfirms.isConfirmed()) {
                long chunkSpace = Peer.getDb().getStoredChunksDb().deleteChunkFromDisk(mostReplicatedChunks.get(0)) / 1000; // Bytes to kBytes

                spaceFreed += chunkSpace;
                System.out.println("Deleted chunk was backed up successfully and disk space was freed on " + chunkSpace + " kB.");
            } else {
                System.out.println("Couldn't delete this chunk, proceeding to another.");
            }

            i++;

            if (spaceFreed >= spaceToReclaim) {
                System.out.println("Peer was able to release " + spaceFreed + " kB, reaching the " + spaceToReclaim + " kB asked for.");
            }

            if (i >= mostReplicatedChunks.size()) {
                System.out.println("Peer ran through all chunks and could not delete chunks in order to reclaim space.");
                return -1;
            }
        }

        return 0;

    }
}
