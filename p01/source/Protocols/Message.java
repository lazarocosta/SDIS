package Protocols;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Message {

    public String header;
    public String body;

    private static String s = " ";
    private static String crlf = "\r\n";   // CarriageReturn == "\r", LineFeed == "\n"

    public String generateHeaderLine(String msgType, String version, int senderId, String fileId, int chunkNum, int replicationDeg){

        /**
         * String template: "<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF>"
         * */

        String line = msgType + s + version + s + Integer.toString(senderId) + s + fileId + s + Integer.toString(chunkNum) + s + Integer.toString(replicationDeg) + crlf;

        return line;
    }

    public String generateHeader(String[] lineArray){

        String header="";

        for(int i = 0; i < lineArray.length; i++)
        {
            header = header + lineArray[i];
        }

        header = header + crlf; // header must end with "<CRLF><CRLF>"

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
