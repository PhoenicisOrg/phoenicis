/**
 *
 */
package org.phoenicis.javafx;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

/**
 * A test for to check that the JavaFX application can be correctly started
 *
 * @author marc
 */
public class AppConfigurationInitialisationTest {

    /**
     * ensures that the test can run in headless mode
     * see https://stackoverflow.com/questions/27403410/headless-testing-with-javafx-and-testfx
     */
    public static void setupHeadless() {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    @Test
    public void testAppConfigurationInitialisation() throws TimeoutException {
        setupHeadless();
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> new AnnotationConfigApplicationContext(AppConfiguration.class));
    }

}
