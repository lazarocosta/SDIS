package protocol;

import files.Chunk;
import systems.Peer;

import java.util.ArrayList;

/**
 * Created by jazz on 29-03-2017.
 */
public class Backup extends SubProtocol{

    public static void sendBackupRequest(ArrayList<Chunk> chunks){

        for(Chunk c: chunks)
        {
            String message = Peer.getUdpChannelGroup().getMDB().messagePutChunk(Peer.getSenderId(), c.getFileId(), c.getChunkNo(), c.getReplicationDegree(), c.getData());
            Peer.getUdpChannelGroup().getMDB().sendsMessage(message);
        }

    }

}
