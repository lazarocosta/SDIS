package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ArrayUtil {

    public static byte[] byteArrayConcat(byte[] first, byte[] second) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(first);
            outputStream.write(second);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] c = outputStream.toByteArray();

        return c;
    }
}
