package org.phoenicis.javafx;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxToolkit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

/**
 * A test for to check that the JavaFX application can be correctly started
 *
 * @author marc
 */
public class AppConfigurationInitialisationTest {

    /**
     * run the test in a temporary application root environment
     */
    private static void setupApplicationRoot() throws IOException {
        Path tempDir = Files.createTempDirectory(AppConfigurationInitialisationTest.class.getName());
        tempDir.toFile().deleteOnExit();
        System.setProperty("application.user.root", tempDir.toString());
    }

    /**
     * ensures that the test can run in headless mode
     * see https://stackoverflow.com/questions/27403410/headless-testing-with-javafx-and-testfx
     */
    private static void setupHeadless() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void testAppConfigurationInitialisation() throws TimeoutException, IOException {
        setupApplicationRoot();
        setupHeadless();
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> new AnnotationConfigApplicationContext(AppConfiguration.class));
    }

}
