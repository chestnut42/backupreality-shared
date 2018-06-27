package com.backupreality.shared.utils;

public class StringUtils
{
    // contains only static methods
    private StringUtils() {}


    /**
     * Converts a HEX digit to integer.
     * This is a copy-paste from {@link javax.xml.bind.DatatypeConverter} implementation.
     * There are some caveats using it directly {@see https://stackoverflow.com/a/5942951/4481904}
     * @param ch is a char to convert to int in range [0, 15]
     * @return an integer in range [0, 15]
     * @throws IllegalArgumentException if the char is not representing a HEX digit
     */
    public static int hexToBin(char ch)
            throws IllegalArgumentException
    {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        }
        if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        }
        throw new IllegalArgumentException("'" + ch + "' is not a HEX digit");
    }


    /**
     * Converts a HEX string to byte array.
     * This is a copy-paste from {@link javax.xml.bind.DatatypeConverter} implementation.
     * There are some caveats using it directly {@see https://stackoverflow.com/a/5942951/4481904}
     * @param s a string to parse
     * @return newly allocated byte array parsed from HEX string {@code s}
     * @throws IllegalArgumentException if the length of string is not divisible by 2 or
     * if the string contains illegal characters
     */
    public static byte[] parseHexBinary(String s)
            throws IllegalArgumentException
    {
        final int len = s.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0) {
            throw new IllegalArgumentException("HEX string needs to be even-length: " + s);
        }

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToBin(s.charAt(i));
            int l = hexToBin(s.charAt(i + 1));
            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }


    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    /**
     * Converts a byte array to HEX string.
     * This is a copy-paste from {@link javax.xml.bind.DatatypeConverter} implementation.
     * There are some caveats using it directly {@see https://stackoverflow.com/a/5942951/4481904}
     * @param data bytes to convert
     * @return HEX string
     */
    public static String printHexBinary(byte[] data)
    {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}

