/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx;

import javafx.application.Platform;
import javafx.scene.text.Font;
import org.phoenicis.javafx.controller.MainController;
import org.phoenicis.multithreading.ControlledThreadPoolExecutorServiceCloser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class JavaFXApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaFXApplication.class);

    private JavaFXApplication() {
        // utility class
    }

    public static void main(String[] args) {
        launchApplication();
    }

    private static void launchApplication() {
        final Runnable startup = () -> {
            try {
                loadFonts();
                final ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                        AppConfiguration.class);

                final MainController mainController = applicationContext.getBean(MainController.class);
                mainController.show();
                mainController.setOnClose(() -> {
                    try {
                        applicationContext.getBean(ControlledThreadPoolExecutorServiceCloser.class)
                                .setCloseImmediately(true);
                        applicationContext.close();
                    } catch (Exception e) {
                        LOGGER.warn("Exception while closing the application.", e);
                    }
                });
            } catch (Exception e) {
                LOGGER.error("Unable to start Phoenicis JavaFX application.", e);
            }
        };

        try {
            Platform.startup(startup);
        } catch (IllegalStateException e) {
            Platform.runLater(startup);
        }
    }

    private static void loadFonts() {
        Font.loadFont(JavaFXApplication.class.getResource("views/common/mavenpro/MavenPro-Medium.ttf").toExternalForm(),
                12);
        Font.loadFont(JavaFXApplication.class.getResource("views/common/roboto/Roboto-Medium.ttf").toExternalForm(),
                12);
        Font.loadFont(JavaFXApplication.class.getResource("views/common/roboto/Roboto-Light.ttf").toExternalForm(),
                12);
        Font.loadFont(JavaFXApplication.class.getResource("views/common/roboto/Roboto-Bold.ttf").toExternalForm(),
                12);
    }
}
