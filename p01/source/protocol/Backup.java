package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import files.MyFile;
import systems.Peer;

import java.util.ArrayList;
import java.util.Random;

/**
 * ENHANCEMENT: Do not store unless desiredReplication degree is not met
 */


public class Backup extends SubProtocol {

    public static boolean enhancements = false;

    public static void backupInitiator(String path, int replicationDegree) {
        System.out.println("Peer is executing backup of file '" + path + "' with replication degree " + replicationDegree);

        MyFile myFile = Backup.saveFileToBackedUpFiles(path, replicationDegree);    // create and save file in initiation server

        sendBackupRequest(myFile.getChunks());
    }

    public static void backupHandler(Message msg) {

        if (!Peer.getDb().getBackedUpFilesDb().containsFileId(msg.getFileId())) {
            Chunk c = new Chunk(msg.getFileId(), msg.getChunkNo(), msg.getReplicationDeg(), msg.getBody());

            storedInitiator(c);
        }

    }

    public static void storedInitiator(Chunk c) {

        if (canPeerStoreChunk(c)) {

            Random randomGenerator = new Random();

            try {
                Thread.sleep(randomGenerator.nextInt(400));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!Peer.getDb().getStoredChunksDb().getStoredData().containsKey(c.getChunkInfo())) // if chunk has not been saved in this Peer ever
            {
                if (Backup.enhancements == true) {
                    System.out.println("Current obtained replication:" + Peer.getDb().getStoredChunksDb().getObtainedReplication().get(c.getChunkInfo()));

                    if (Peer.getDb().getStoredChunksDb().getObtainedReplication().get(c.getChunkInfo()) == null ||
                            Peer.getDb().getStoredChunksDb().getObtainedReplication().get(c.getChunkInfo()) < c.getReplicationDegree()) {
                        saveChunk(c);
                        storeChunk(c);
                        sendStoredMessage(c);
                    }
                    // else
                    // do nothing
                } else {
                    saveChunk(c);
                    storeChunk(c);
                    sendStoredMessage(c);
                }
            }
        }
    }

    public static void storedHandler(Message msg) {

        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        Peer.getDb().getStoredChunksDb().incrementReplicationObtained(chunkInfo);

        String filepath;
        if ((filepath = Peer.getDb().getBackedUpFilesDb().fileIdToFilePath(msg.getFileId())) != null) {   // if the Peer was the initiator in the backup of the Chunk
            System.out.println("Chunk from " + filepath + " with number " + msg.getChunkNo() + " has been saved by server with id " + msg.getSenderId());
        }
    }

    private static MyFile saveFileToBackedUpFiles(String filepath, int replicationDegree) {

        MyFile myFile = new MyFile(filepath, replicationDegree);
        myFile.divideFileIntoChunks();  // create chunks and store them
        Peer.getDb().saveBackedUpFile(myFile);

        //  System.out.println(Peer.getDb().getBackedUpFiles());

        //myFile.saveCopy();

        return myFile;
    }

    public static void sendBackupRequest(ArrayList<Chunk> chunks) {

        System.out.println("Sending backup request.");

        for (Chunk c : chunks) {
            VerifyStoredConfirms verifyStoredConfirms = new VerifyStoredConfirms(c);
            new Thread(verifyStoredConfirms).start();
        }
    }

    private static void storeChunk(Chunk c) {
        Peer.getDb().getStoredChunksDb().addChunk(c);
    }

    private static void sendStoredMessage(Chunk c) {

        Peer.getUdpChannelGroup().sendForControl(Peer.getUdpChannelGroup().getMC().messageStored(Peer.getSenderId(), c.getFileId(), c.getChunkNo()));

    }

    private static void saveChunk(Chunk c) {
        Peer.getDb().getStoredChunksDb().saveChunkToDisk(c);
    }

    private static boolean canPeerStoreChunk(Chunk c) {
        return (Peer.getDb().getBackedUpFilesDb().fileIdToFilePath(c.getFileId()) == null);
    }

    public static class VerifyStoredConfirms implements Runnable {

        private static final int INITIAL_INTERVAL = 1000; // 1 seg = 1000 ms
        private static final int MAX_TRIES = 5;

        boolean confirmed = false;

        public boolean isConfirmed() {
            return confirmed;
        }

        private Chunk chunk;

        public VerifyStoredConfirms(Chunk chunk) {
            this.chunk = chunk;
        }

        @Override
        public void run() {

            int interval = INITIAL_INTERVAL;
            int tries = 0;

            byte[] message = Peer.getUdpChannelGroup().getMDB().messagePutChunk(Peer.getSenderId(), chunk);


            while (!confirmed && tries < MAX_TRIES) {

                Peer.getUdpChannelGroup().getMDB().sendsMessage(message);

                try {
                    System.out.println("Waited for " + interval + " ms");
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int numberOfConfirms = 0;

                if (Peer.getDb().getStoredChunksDb().getObtainedReplication().get(this.chunk.getChunkInfo()) != null)
                    numberOfConfirms = Peer.getDb().getStoredChunksDb().getObtainedReplication().get(this.chunk.getChunkInfo());

                System.out.println("Number of confirms of chunk no." + this.chunk.getChunkNo() + " during the interval: " + numberOfConfirms);

                if (numberOfConfirms < this.chunk.getReplicationDegree()) {
                    Peer.getDb().getStoredChunksDb().resetReplicationObtained(this.chunk.getChunkInfo());
                    tries++;
                    interval = interval * 2;

                    if (tries == MAX_TRIES) {
                        System.out.println("Reached maximum tries to backup chunk with no." + this.chunk.getChunkNo() + " with desired replication degree.");
                    }
                } else {
                    confirmed = true;
                    System.out.println("Desired replication reached for chunk with no." + this.chunk.getChunkNo() +".");
                }

            }

        }

    }

}
