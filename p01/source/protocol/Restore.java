package protocol;
import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;

import java.io.File;
import java.util.Random;

public class Restore extends SubProtocol {

    private final String FILE_RESTORE_DIR = "CHUNKS/peer" + Peer.getSenderId() + "/";

    public static void restoreInitiator(String path) {

        if (Peer.getDb().getBackedUpFilesDb().containsPath(path)) {

            System.out.println("Peer is executing restore of file '" + path);

            ChunkInfo chunkInfo = Peer.getDb().getBackedUpFilesDb().getChunkInfo(path);
            String fileId = chunkInfo.getFileId();
            int numberChunks = chunkInfo.getChunkNo();

            sendRestoreRequest(fileId, numberChunks);

            endsRestore(path,fileId, numberChunks);
        } else
            System.out.println("Peer without file");


    }

    private static void endsRestore(String path,String fileId, int numberChunks) {

        File file = new File(path);


        for(int i =1; i < numberChunks; i++){
            ChunkInfo chunkInfo = new ChunkInfo(fileId, i);
            if(Peer.getDb().getRestoredChunkDd().containsKey(chunkInfo)){

            }
        }

      //  ChunkInfo chunkInfo = new ChunkInfo(fileId, i);


    }

    private static void sendRestoreRequest(String fileId, int numberChunks) {

        System.out.println("Send Restore Request.");
        for (int i = 1; i <= numberChunks; i++) {
            System.out.println("Message is: " + Peer.getSenderId() + ";" + fileId + ";" + "ChunkNo: " + i + ";");

            byte[] message = Peer.getUdpChannelGroup().getMC().messageGetChunk(Peer.getSenderId(), fileId, i);
            Peer.getUdpChannelGroup().getMC().sendsMessage(message);

            ChunkInfo chunkInfo = new ChunkInfo(fileId, i);

           // VerifyRestoreConfirms verifyRestoreConfirms = new VerifyRestoreConfirms(chunkInfo);
         //   verifyRestoreConfirms.run();
        }

    }

    //__________________________________
    public static void getChunkHandler(Message msg) {

        System.out.println("Message received on getChunkHandler: " + msg.toString());
        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        if (Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {

            Random randomGenerator = new Random();

            Peer.getUdpChannelGroup().getMDR().sleep(randomGenerator.nextInt(400));
            sendChunkMessage(chunkInfo);

        } else {
            System.out.println("This server did'not restore the file" + msg.getFileId() + "'.");
        }
    }

    private static void sendChunkMessage(ChunkInfo c) {

        if (!Peer.getDb().getResponseRestore().contains(c)) {

            byte[] data = Peer.getDb().getStoredChunksDb().getStoredChunks().get(c);
            Peer.getUdpChannelGroup().sendForRestore(Peer.getUdpChannelGroup().getMDR().messageChunk(c.getFileId(), c.getChunkNo(), data));

        } else {
            Peer.getDb().getResponseRestore().remove(c);
            System.out.println("Answered by another");
        }
    }

    //______________________________
    public static void chunkHandler(Message msg) {

        System.out.println("Message received on chunkHandler: " + msg.toString());
        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        if (Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {
            if (!Peer.getDb().getResponseRestore().contains(chunkInfo)) {
                Peer.getDb().getResponseRestore().add(chunkInfo);
                System.out.println("This server receive response");
            }
        } else if (Peer.getDb().getBackedUpFilesDb().containsFileId(chunkInfo.getFileId())) {
            Peer.getDb().getRestoredChunkDd().put(chunkInfo, msg.getBody());
            System.out.println("This server initiator receive chunk");

        } else {
            System.out.println("This server did not make any interaction in this file" + msg.getFileId() + "'.");
        }
        //Para teste do erro
        if(Peer.getSenderId()==1){
         System.out.println(Peer.getDb().getStoredChunksDb().getStoredChunks());
            System.out.println(Peer.getDb().getBackedUpFilesDb().containsFileId(chunkInfo.getFileId()));
        }

    }


    public static class VerifyRestoreConfirms implements Runnable {

        private static final int INITIAL_INTERVAL = 1000; // 1 seg = 1000 ms
        private static final int MAX_TRIES = 5;

        private ChunkInfo chunk;

        public VerifyRestoreConfirms(ChunkInfo chunk) {
            this.chunk = chunk;
        }

        @Override
        public void run() {

            int interval = INITIAL_INTERVAL;
            int tries = 0;
            boolean confirmed = false;

            while (!confirmed && tries < MAX_TRIES) {
                try {
                    System.out.println("Waited for " + interval + " ms");
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*int numberOfConfirms = Peer.getDb().getStoredChunksDb().getObtainedReplication().get(this.chunk.getChunkInfo());

                System.out.println("Number of confirms during the interval: " + numberOfConfirms);

                if (numberOfConfirms < this.chunk.getReplicationDegree()) {
                    Peer.getDb().getStoredChunksDb().resetReplicationObtained(this.chunk.getChunkInfo());
                    tries++;
                    interval = interval * 2;

                    if (tries == MAX_TRIES) {
                        System.out.println("Reached maximum tries to backup chunk with desired replication degree.");
                    }
                } else {
                    confirmed = true;
                    System.out.println("Desired replication reached for chunk.");
                }*/

            }

        }
    }
}

