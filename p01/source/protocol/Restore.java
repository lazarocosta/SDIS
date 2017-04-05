package protocol;

import systems.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jazz on 29-03-2017.
 */
public class Restore extends SubProtocol {


    public byte[] getChunkOfSender(String fileId, int chunkNo) throws IOException {

        String pathSenderId = "Sender" + Peer.getSenderId();
        String pathChunkNo = pathSenderId + "/" + fileId + "/" + chunkNo + ".txt"; // TEMOS QUE VER AQUI A TERMINAÇAO

        byte[] body;

        File f = new File(pathChunkNo);

        if (f.exists()) {

            System.out.println("existe chunk");
            InputStream is = new FileInputStream(pathChunkNo);
            int size = is.available();


            body = new byte[size];

            int re = is.read(body, 0, size);

            System.out.println(body.toString());
        } else return null;

        return body;
    }
}
