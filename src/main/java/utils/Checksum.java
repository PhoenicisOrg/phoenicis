package utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Checksum {
    private static final int BLOCK_SIZE = 2048;

    public static String calculate(File fileToCheck, String algorithm) throws NoSuchAlgorithmException, IOException {
        FileInputStream inputStream = new FileInputStream(fileToCheck);
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        return Hex.encodeHexString(getDigest(inputStream, messageDigest));
    }

    private static byte[] getDigest(InputStream inputStream, MessageDigest messageDigest)
            throws NoSuchAlgorithmException, IOException {

        messageDigest.reset();
        byte[] bytes = new byte[BLOCK_SIZE];
        int numBytes;
        while ((numBytes = inputStream.read(bytes)) != -1) {
            messageDigest.update(bytes, 0, numBytes);
        }
        byte[] digest = messageDigest.digest();
        return digest;
    }
}
