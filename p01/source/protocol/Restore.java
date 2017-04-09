package protocol;

import chunk.ChunkInfo;
import systems.Peer;
import utils.ArrayUtil;

import java.io.*;
import java.util.Random;


public class Restore extends SubProtocol {

    private static String FILE_RESTORE_DIR = "RESTORED/peer" + Peer.getSenderId();

    public static byte[] restoreInitiator(String path) {

        byte[] b;
        if (Peer.getDb().getBackedUpFilesDb().containsPath(path)) {

            System.out.println("Peer is executing restore of file '" + path);

            ChunkInfo chunkInfo = Peer.getDb().getBackedUpFilesDb().getChunkInfo(path);
            String fileId = chunkInfo.getFileId();
            int numberChunks = chunkInfo.getChunkNo();

            sendRestoreRequest(fileId, numberChunks);

            b = endsRestore(path, fileId, numberChunks);
        } else {
            b = new byte[0];
            System.out.println("Peer did not backup file'" + path + "'.");
        }

        return b;
    }

    private static byte[] endsRestore(String fileName, String fileId, int numberChunks) {

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Peer initiate the restore");
        System.out.println("number Chunks: " + numberChunks);

        byte[] file = new byte[0];

        for (int i = 1; i <= numberChunks; i++) {
            ChunkInfo chunkInfo = new ChunkInfo(fileId, i);
            System.out.println(chunkInfo.getChunkNo() + "_________");
            if (Peer.getDb().getRestoredChunkDd().containsKey(chunkInfo)) {
                System.out.println(chunkInfo.getChunkNo() + "______+++++++++++___");
                byte[] data = Peer.getDb().getRestoredChunkDd().get(chunkInfo);

                file = ArrayUtil.byteArrayConcat(file, data);
            }
        }
        //TODO: Só para testar
        saveRestoredFile(fileName, file);

        return file;
    }

    public static void saveRestoredFile(String fileName, byte[] data) {

        File path = new File(FILE_RESTORE_DIR);
        String dir = path + "/" + fileName;

        if (!path.exists()) {
            path.mkdirs();
        }

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dir);
            out.write(data);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    //
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

            byte[] data = Peer.getDb().getStoredChunksDb().getStoredData().get(c);
            Peer.getUdpChannelGroup().sendForRestore(Peer.getUdpChannelGroup().getMDR().messageChunk(c.getFileId(), c.getChunkNo(), data));

        } else {
            Peer.getDb().getResponseRestore().remove(c);
            System.out.println("Answered by another");
        }
    }

    //
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

