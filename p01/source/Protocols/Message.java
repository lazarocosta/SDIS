package Protocols;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Message {

    // TODO: Put Body on some messages and not on every single one.


    public String header;
    public String body;

    private static final String SPACE = " ";
    private static final String CRLF = "\r\n";   // CarriageReturn == "\r", LineFeed == "\n"


    public String generatePutChunkLine(String version, int senderId, String fileId, int chunkNum, int replicationDeg)
    {
        return generateHeaderLine("PUTCHUNK", version, senderId, fileId, chunkNum, replicationDeg);
    }

    public String generateStoredLine(String version, int senderId, String fileId, int chunkNum)
    {
            return generateHeaderLine("STORED", version, senderId, fileId, null, null);
    }

    public String generateGetChunkLine(String version, int senderId, String fileId, int chunkNum)
    {
        return generateHeaderLine("GETCHUNK", version, senderId, fileId, null, null);
    }

    public String generateHeaderLine(String msgType, String version, Integer senderId, String fileId, Integer chunkNum, Integer replicationDeg){

        /**
         * String template: "<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>"
         * */

        String line = msgType + SPACE + version + SPACE + senderId.toString() + SPACE + fileId + SPACE + chunkNum.toString() + SPACE + replicationDeg.toString() + CRLF;

        return line;
    }

    public String generateHeader(String[] lineArray){

        String header="";

        for(int i = 0; i < lineArray.length; i++)
        {
            header = header + lineArray[i];
        }

        header = header + CRLF; // header must end with "<CRLF><CRLF>"

        return header;
    }

    public String getHashFromString(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(str.getBytes("UTF-8")); // Change this to "UTF-16" if needed
        byte[] digest = md.digest();

        return DatatypeConverter.printHexBinary(digest).toLowerCase();

    }



    // For testing
    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        Message message = new Message();

        System.out.println(message.getHashFromString("123"));
    }

}
