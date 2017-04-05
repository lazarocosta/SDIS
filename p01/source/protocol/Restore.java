package protocol;

import chunk.Chunk;
import chunk.ChunkInfo;
import systems.Peer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jazz on 29-03-2017.
 */
public class Restore extends SubProtocol {




    public static void restoreChunk(String fileId, int chunkNo) throws IOException {

       // String pathSenderId = "Sender" + Peer.getSenderId();
        //String pathChunkNo = pathSenderId + "/" + fileId + "/" + chunkNo + ".txt"; //

        byte[] body;

        //File f = new File(pathChunkNo);

        ChunkInfo chunkInfo = new ChunkInfo(fileId, chunkNo);


        if ( Peer.getDb().getStoredChunksDb().existsChunkInfo(chunkInfo)) {
          //  InputStream is = new FileInputStream(pathChunkNo);
            //int size = is.available();


            body =  Peer.getDb().getStoredChunksDb().getBodyChunk(chunkInfo);
          //  int re = is.read(body, 0, size);

            System.out.println(body.toString());

            sendChunkMessage(chunkInfo,body);
        } else{
            System.out.println("Don't exists file");
        }

    }

    private static void sendChunkMessage(ChunkInfo c, byte[] body){

        System.out.println(body);
        Peer.getUdpChannelGroup().sendForRestore(Peer.getUdpChannelGroup().getMDR().messageChunk( c.getFileId(), c.getChunkNo(),body ));

    }
}
