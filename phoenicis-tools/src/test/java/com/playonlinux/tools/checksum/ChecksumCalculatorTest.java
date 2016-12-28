package com.playonlinux.tools.checksum;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatorTest {
    @Test
    public void testChecksumCalculate_generateAFile_CheckMD5() throws IOException {
        File temporaryFile = File.createTempFile("testHash", "txt");

        try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
            fileOutputStream.write("TEST".getBytes());
        }

        assertEquals("033bd94b1168d7e4f0d644c3c95e35bf", new ChecksumCalculator().calculate(temporaryFile, "MD5", e -> {}));
    }

    @Test
    public void testChecksumCalculate_generateAFile_CheckSHA1() throws IOException {
        File temporaryFile = File.createTempFile("testHash", "txt");

        try (FileOutputStream fileOutputStream = new FileOutputStream(temporaryFile)) {
            fileOutputStream.write("TEST".getBytes());
        }

        assertEquals("984816fd329622876e14907634264e6f332e9fb3",
                new ChecksumCalculator().calculate(temporaryFile, "SHA1", e -> {}));
    }
}