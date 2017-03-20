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

    /**
     *
     * @param version This is the version of the PROTOCOL. It is a three ASCII char sequence with the format <n>'.'<m>, where <n> and <m> are the ASCII codes of digits.
     *                For example, version 1.0, the one specified in this document, should be encoded as the char sequence '1''.''0'.
     * @param senderId This is the id of the server that has sent the message. This field is useful in many subprotocols. This is encoded as a variable length sequence of ASCII digits.
     * @param fileId This is the file identifier for the backup service. As stated above, it is supposed to be obtained by using the SHA256 cryptographic hash function.
     * @param chunkNum This field together with the FileId specifies a chunk in the file. The chunk numbers are integers and should be assigned sequentially starting at 0.
     * @param replicationDeg This field contains the desired replication degree of the chunk. This is a digit, thus allowing a replication degree of up to 9.
     * @return Generated message in a String field.
     */
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

        System.out.println(unEscapeString(message.generateHeader(new String[]{message.generatePutChunkLine("1.0", 1, "23hj123", 0, 2)})));
    }

    public static String unEscapeString(String s){
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<s.length(); i++)
            switch (s.charAt(i)){
                case '\n': sb.append("\\n"); break;
                case '\t': sb.append("\\t"); break;
                case '\r': sb.append("\\r"); break;
                // ... rest of escape characters
                default: sb.append(s.charAt(i));
            }
        return sb.toString();
    }

}
