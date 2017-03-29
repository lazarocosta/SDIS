package protocol;

import systems.Peer;

import java.io.File;

/**
 * Created by jazz on 29-03-2017.
 */
public class Delete extends SubProtocol{

    public static void deleteFile(String fileId) {

        String pathSenderId = "Sender" + Peer.getSenderId();
        String pathFileId = pathSenderId + "/" + fileId;

        File f = new File(pathFileId);
        File fileSender = new File(pathSenderId);

        if (f.exists()) {
            for (File file : f.listFiles()) {
                file.delete();
                System.out.println("delete file into" + pathFileId);
            }

            for (File file : fileSender.listFiles()) {
                if (file.compareTo(f) == 0) {//equals
                    file.delete();
                    System.out.println("delete diretory" + pathFileId);
                }
            }
        }


    }
}
