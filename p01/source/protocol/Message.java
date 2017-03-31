package protocol;

import files.Chunk;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Message {

    // TODO: Put Body on some messages and not on every single one.

    public String header;
    public byte[] body;

    private static final String SPACE = " ";
    private static final String CRLF = "\r\n";   // CarriageReturn == "\r", LineFeed == "\n"

    private String version;
    private int senderId;
    private String fileId;
    private int chunkNo;
    private int replicationDeg;
    private String msgType;

    // Message constructors
    public Message(int senderId, String fileId, int chunkNo, int replicationDeg) {
        this(senderId, fileId, chunkNo);
        this.replicationDeg = replicationDeg;
    }

    public Message(int senderId, String fileId, int chunkNo) {

        this(senderId, fileId);
        this.chunkNo = chunkNo;
    }

    public Message(int senderId, String fileId) {

        this();
        this.senderId = senderId;
        this.fileId = fileId;
    }

    public Message(){
        this.version = SubProtocol.getVersion();
    }


    public String msgPutChunk() {

        String header = generateHeaderLine("PUTCHUNK", this.version, this.senderId, this.fileId, this.chunkNo, this.replicationDeg);
        header += this.body;

        return header;
    }

    public String msgGetChunk() {
        System.out.println("mensagems" + senderId);
        return generateHeaderLine("GETCHUNK", this.version, this.senderId, this.fileId, this.chunkNo, null);
    }

    public String msgStored() {
        return generateHeaderLine("STORED", this.version, this.senderId, this.fileId, this.chunkNo, null);
    }

    public String msgChunk() {
        String header = generateHeaderLine("CHUNK", this.version, this.senderId, this.fileId, this.chunkNo, null);
        header += this.body;

        return header;
    }

    public String msgDelete() {
        return generateHeaderLine("DELETE", this.version, this.senderId, this.fileId, null, null);
    }

    public String msgRemoved() {
        return generateHeaderLine("REMOVED", this.version, this.senderId, this.fileId, this.chunkNo, null);
    }

    public void setBody(byte[]body) {
        this.body = body;
    }

    public String getVersion() {
        return version;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getFileId() {
        return fileId;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public String getMsgType() {
        return msgType;
    }

    public byte[] getBody() {
        return body;
    }


    private String generateHeaderLine(String msgType, String version, Integer senderId, String fileId, Integer chunkNo, Integer replicationDeg) {

        /**
         * String template: "<MessageType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF>"
         * */
        String line;
        if (replicationDeg == null) {
            if (chunkNo == null) {
                line = "" + msgType + SPACE + version + SPACE + senderId.toString() + SPACE + fileId + CRLF + CRLF;
            } else
                line = "" + msgType + SPACE + version + SPACE + senderId.toString() + SPACE + fileId + SPACE + chunkNo.toString() + CRLF + CRLF;
        } else
            line = "" + msgType + SPACE + version + SPACE + senderId.toString() + SPACE + fileId + SPACE + chunkNo.toString() + SPACE + replicationDeg.toString() + CRLF + CRLF;

        return line;
    }

    public String generateHeader(String[] lineArray) {

        String header = Arrays.toString(lineArray);
        header = header + CRLF + CRLF; // header must end with "<CRLF><CRLF>"

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

        //Message message = new Message();

        // System.out.println(unEscapeString(message.generateHeader(new String[]{message.generatePutChunkLine("1.0", 1, "23hj123", 0, 2)})));
    }

    public static String unEscapeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            switch (s.charAt(i)) {
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                // ... rest of escape characters
                default:
                    sb.append(s.charAt(i));
            }
        return sb.toString();
    }

    public void separateFullMsg(String message) {

        // PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
        // CHUNK <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
        String[] tokens = message.split(CRLF + CRLF);

        this.body = tokens[1].getBytes();
        String[] header = tokens[0].split("\\s+");

        if (header.length >= 5) {
            this.msgType = header[0];
            this.version = header[1];
            this.senderId = Integer.parseInt(header[2]);
            this.fileId = header[3];
            this.chunkNo = Integer.parseInt(header[4]);
        }
        if (header.length >= 6) {
            this.replicationDeg = Integer.parseInt(header[5]);
        }
    }

    public void separateMsg(String message) {

        // <msgType> <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF>
        // <msgType> <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
        // <msgType> <Version> <SenderId> <FileId> <CRLF><CRLF>

        String[] header = message.split("\\s+");

        if (header.length >= 4) {
            this.msgType = header[0];
            this.version = header[1];
            this.senderId = Integer.parseInt(header[2]);
            this.fileId = header[3];

            if (header.length >= 5) {
                this.chunkNo = Integer.parseInt(header[4]);

                if (header.length >= 6) {
                    this.replicationDeg = Integer.parseInt(header[5]);
                }

            }
        }
    }


}
