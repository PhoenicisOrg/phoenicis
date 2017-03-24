/**
 * 
 */
package org.phoenicis.javafx;

import java.util.concurrent.TimeoutException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxToolkit;

/**
 * @author marc
 *
 */
public class AppConfigurationInitialisationTest {

	@Test
	public void testAppConfigurationInitialisation() throws TimeoutException {
		FxToolkit.registerPrimaryStage();
		FxToolkit.setupFixture(() -> new AnnotationConfigApplicationContext(AppConfiguration.class));
	}

}
