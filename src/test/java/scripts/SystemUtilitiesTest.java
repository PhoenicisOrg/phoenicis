package scripts;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class SystemUtilitiesTest {
    @Test
    public void testGetFreeSpace_compareWithDf() throws IOException, InterruptedException {
        String directory = "/";

        String command = "df -P -k "+directory+"";
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();
        long actualValue = SystemUtilities.getFreeSpace(directory);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        reader.readLine();
        String firstList = reader.readLine();

        long expectedValue = Integer.valueOf(firstList.split("[ .]+")[3]);


        assertEquals(expectedValue, actualValue);
    }
}