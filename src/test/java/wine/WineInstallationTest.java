package wine;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WineInstallationTest {

    private WineInstallation wineInstallationToTest;

    @Before
    public void getSystemProperties() throws Exception {
        URL url = this.getClass().getResource(".");
        this.wineInstallationToTest = new WineInstallation.Builder().withPath(new File(url.getPath())).build();
    }

    @Test
    public void testRun_RunWineVersion_ProcessRunsAndReturnsVersion() throws IOException {
        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "--version", null);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertEquals("wine-1.7.26\n", processOutput);
    }

    @Test
    public void testRun_RunWineVersionWithArgument_ProcessReturnsHelpMessage() throws IOException {
        ArrayList <String> arguments = new ArrayList<>();
        arguments.add("/tmp/unexisting");

        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "--help", null, arguments);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertEquals("Usage: wine PROGRAM [ARGUMENTS...]   Run the specified program\n" +
                "       wine --help                   Display this help and exit\n" +
                "       wine --version                Output version information and exit\n", processOutput);
    }

    @Test
    public void testRun_RunWineVersionWithArgument_ProcessDoesNotReturnHepMessage() throws IOException {
        ArrayList <String> arguments = new ArrayList<>();
        arguments.add("--help");

        Process wineProcess = this.wineInstallationToTest.run(new File("/tmp"), "/tmp/unexisting", null, arguments);

        InputStream inputStream = wineProcess.getInputStream();
        String processOutput = IOUtils.toString(inputStream);

        assertNotEquals("Usage: wine PROGRAM [ARGUMENTS...]   Run the specified program\n" +
                "       wine --help                   Display this help and exit\n" +
                "       wine --version                Output version information and exit\n", processOutput);
    }



}