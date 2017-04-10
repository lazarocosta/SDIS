package protocol;

import chunk.ChunkInfo;
import systems.Peer;

public class Delete extends SubProtocol {
    private static int ATTEMPTS = 5;
    private static long SLEEP_ms = 100;

    public static void initiator(String filePath) {

        if (Peer.getDb().getBackedUpFilesDb().containsPath(filePath)) {

            String fileId = Peer.getDb().getBackedUpFilesDb().getFileId(filePath);

            //   Delete.deleteBackupFile(filePath);

            int attempts = ATTEMPTS;

            while (attempts > 0) {

                byte[] packetDelete = Peer.getUdpChannelGroup().getMC().messageDelete(Peer.getSenderId(), fileId);
                Peer.getUdpChannelGroup().getMC().sendsMessage(packetDelete);

                try {
                    Thread.sleep(SLEEP_ms);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                attempts--;
            }

        } else System.out.println("Does not have the file backup");
    }

    public static void deleteFile(Message msg) {
        String fileId = msg.getFileId();

        if (Peer.getDb().getBackedUpFilesDb().containsFileId(fileId)) {
            System.out.println("Peer will eliminate the file from BackedUpFilesDatabase");

            String path = Peer.getDb().getBackedUpFilesDb().fileIdToFilePath(fileId);
            deleteFilesBackup(path);

        } else if (storedFile(fileId)) {
            System.out.println("Peer will eliminate the file from StoredChunksDatabase");
            deleteFilesDatabase(fileId);
        } else {
            System.out.println("This server did not backup the file" + fileId + "'.");
        }
    }


    private static void deleteFilesDatabase(String fileId) {

        for (ChunkInfo chunkInfo : Peer.getDb().getStoredChunksDb().getStoredData().keySet()) {
            if (chunkInfo.getFileId().equals(fileId)) {
                Peer.getDb().getStoredChunksDb().deleteChunkFromDisk(chunkInfo);
            }
        }
    }

    private static boolean storedFile(String fileId) {
        for (ChunkInfo chunkInfo : Peer.getDb().getStoredChunksDb().getStoredData().keySet())
            if (chunkInfo.getFileId().equals(fileId))
                return true;

        return false;
    }

    public static void deleteFilesBackup(String path) {
        Peer.getDb().getBackedUpFilesDb().removeFileByPath(path);
    }
}
