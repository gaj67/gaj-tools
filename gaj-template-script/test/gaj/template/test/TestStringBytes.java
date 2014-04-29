/*
 * (c) Geoff Jarrad, 2013.
 */
package gaj.template.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TestStringBytes {

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.printf("String \"%s\" -> bytes %s%n", arg, toByteString(arg));
        }
    }

    private static String toByteString(String s) {
        List<Byte> bytes = new ArrayList<>();
        for (byte b : toBytes(s)) bytes.add(b);
        return bytes.toString();
    }

    private static byte[] toBytes(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

}
