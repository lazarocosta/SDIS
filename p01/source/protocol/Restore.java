package protocol;

import chunk.ChunkInfo;
import systems.Peer;
import utils.ArrayUtil;

import java.io.*;
import java.net.InetAddress;
import java.util.Random;

public class Restore extends SubProtocol {

    public static boolean enhancements = false;

    private static String FILE_RESTORE_DIR = "RESTORED/peer" + Peer.getSenderId();

    public static byte[] restoreInitiator(String path) {

        byte[] b;
        if (Peer.getDb().getBackedUpFilesDb().containsPath(path)) {

            System.out.println("Peer is executing restore of file '" + path +"'.");

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

        byte[] file = new byte[0];

        int confirmations = Peer.getDb().getRestoreUpFilesDb().getConfirmationFile().get(fileId);

        while (confirmations != numberChunks && confirmations != -1) {

            confirmations = Peer.getDb().getRestoreUpFilesDb().getConfirmationFile().get(fileId);

        }

        if (confirmations == -1) {
            Peer.getDb().getRestoreUpFilesDb().resetConfirmationFile(fileId);
            System.out.println("Peer did not receive complete file");
            return null;
        }

        for (int i = 1; i <= numberChunks; i++) {
            ChunkInfo chunkInfo = new ChunkInfo(fileId, i);
            System.out.println(chunkInfo.getChunkNo() + "_________");
            if (Peer.getDb().getRestoreUpFilesDb().getRestoredChunkDd().containsKey(chunkInfo)) {
                System.out.println(chunkInfo.getChunkNo() + "______+++++++++++___");
                byte[] data = Peer.getDb().getRestoreUpFilesDb().getRestoredChunkDd().get(chunkInfo);

                file = ArrayUtil.byteArrayConcat(file, data);
            }
        }

        Peer.getDb().getRestoreUpFilesDb().resetConfirmationFile(fileId);
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
        for (int i = 1; i <= numberChunks; i++) {

            ChunkInfo chunkInfo = new ChunkInfo(fileId, i);

            VerifyRestoreConfirms verifyRestoreConfirms = new VerifyRestoreConfirms(chunkInfo);
            verifyRestoreConfirms.run();
        }

    }

    public static void getChunkHandler(Message msg) {

        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        if (Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {

            SendChunkMessage sendChunkMessage = new SendChunkMessage(chunkInfo);
            sendChunkMessage.run();

        } else {
            System.out.println("This server did not backup the file: " + msg.getFileId() + "'.");
        }
    }

    public static void getChunkHandler(Message msg, InetAddress originPeerIp, int originPeerPort) {

        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        if (Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {
            SendChunkMessage sendChunkMessage = new SendChunkMessage(chunkInfo, originPeerIp, originPeerPort);
            sendChunkMessage.run();

        } else {
            System.out.println("This server backed up the file: " + msg.getFileId() + "'.");
        }
    }

    public static void chunkHandler(Message msg) {

        ChunkInfo chunkInfo = new ChunkInfo(msg.getFileId(), msg.getChunkNo());

        if (Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {
            if (!Peer.getDb().getRestoreUpFilesDb().getResponseRestore().contains(chunkInfo)) {
                Peer.getDb().getRestoreUpFilesDb().getResponseRestore().add(chunkInfo);
            }
        } else if (Peer.getDb().getBackedUpFilesDb().containsFileId(chunkInfo.getFileId())) {
            Peer.getDb().getRestoreUpFilesDb().getRestoredChunkDd().put(chunkInfo, msg.getBody());

        } else {
            System.out.println("This server did not make any interaction with this file" + msg.getFileId() + "'.");
        }


    }

    public static class VerifyRestoreConfirms implements Runnable {

        private static final int INITIAL_INTERVAL = 1000; // 1 seg = 1000 ms
        private static final int MAX_TRIES = 5;

        private ChunkInfo chunkInfo;

        public VerifyRestoreConfirms(ChunkInfo chunk) {
            this.chunkInfo = chunk;
        }

        @Override
        public void run() {

            int interval = INITIAL_INTERVAL;
            int tries = 0;
            boolean confirmed = false;

            byte[] message = Peer.getUdpChannelGroup().getMC().messageGetChunk(Peer.getSenderId(), chunkInfo.getFileId(), chunkInfo.getChunkNo());

            while (!confirmed && tries < MAX_TRIES) {

                Peer.getUdpChannelGroup().getMC().sendsMessage(message);

                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (Peer.getDb().getRestoreUpFilesDb().getRestoredChunkDd().containsKey(chunkInfo)) {
                    Peer.getDb().getRestoreUpFilesDb().addConfirmationFile(chunkInfo.getFileId());
                    confirmed = true;
                } else {
                    tries++;
                    interval *= 2;

                    if (tries == MAX_TRIES) {
                        Peer.getDb().getRestoreUpFilesDb().addConfirmationErrorFile(chunkInfo.getFileId());

                        System.out.println("Reached maximum tries to restore chunkNo: " + chunkInfo.getChunkNo());
                    }
                }
            }
        }
    }

    public static class SendChunkMessage implements Runnable {

        private ChunkInfo chunkInfo;
        private InetAddress originPeerIp = null;
        private int originPeerPort = -1;

        public SendChunkMessage(ChunkInfo chunkInfo) {
            this.chunkInfo = chunkInfo;
        }

        public SendChunkMessage(ChunkInfo chunkInfo, InetAddress originPeerIp, int originPeerPort) {
            this.chunkInfo = chunkInfo;
            this.originPeerIp = originPeerIp;
            this.originPeerPort = originPeerPort;
        }

        @Override
        public void run() {

            if (!Peer.getDb().getRestoreUpFilesDb().getResponseRestore().contains(chunkInfo)) {

                Random randomGenerator = new Random();

                try {
                    Thread.sleep(randomGenerator.nextInt(400));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                byte[] data = Peer.getDb().getStoredChunksDb().getStoredData().get(chunkInfo);

                byte[] packet = Peer.getUdpChannelGroup().getMDR().messageChunk(chunkInfo.getFileId(), chunkInfo.getChunkNo(), data);

                if (this.originPeerIp == null || this.originPeerPort == -1)   // if the peer IP was not passed, send to multicast
                    Peer.getUdpChannelGroup().sendForRestore(packet);
                else {
                    Peer.getUdpChannelGroup().getMDR().sendsMessageToSpecificPeer(packet, this.originPeerIp, this.originPeerPort);
                }

            } else {
                Peer.getDb().getRestoreUpFilesDb().getResponseRestore().remove(chunkInfo);
            }
        }
    }
}

