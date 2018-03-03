/**
 * 
 */
package org.phoenicis.javafx;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxToolkit;

import java.util.concurrent.TimeoutException;

/**
 * @author marc
 *
 */
public class AppConfigurationInitialisationTest {

    @Before
    public void setup() {
        System.setProperty("testfx.headless", "false");
    }

    @Test
    public void testAppConfigurationInitialisation() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupFixture(() -> new AnnotationConfigApplicationContext(AppConfiguration.class));
    }

}
