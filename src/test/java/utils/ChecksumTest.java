package utils;

import org.junit.Test;
import utils.Checksum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public class ChecksumTest {
    @Test
    public void testChecksumCalculate_generateAFile_CheckMD5() throws IOException, NoSuchAlgorithmException {
        File temporaryFile = File.createTempFile("testHash", "txt");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);

        fileOutputStream.write("TEST".getBytes());
        assertEquals("033bd94b1168d7e4f0d644c3c95e35bf", Checksum.calculate(temporaryFile, "MD5"));
    }

    @Test
    public void testChecksumCalculate_generateAFile_CheckSHA1() throws IOException, NoSuchAlgorithmException {
        File temporaryFile = File.createTempFile("testHash", "txt");
        FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile);

        fileOutputStream.write("TEST".getBytes());
        assertEquals("984816fd329622876e14907634264e6f332e9fb3", Checksum.calculate(temporaryFile, "SHA1"));
    }
}
