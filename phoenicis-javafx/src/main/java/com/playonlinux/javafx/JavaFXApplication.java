/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.javafx;

import com.playonlinux.javafx.controller.MainController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaFXApplication extends Application {

    public static void main(String[] args) {
        Application.launch(JavaFXApplication.class);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("views/common/playonlinux.png")));
        primaryStage.setTitle("PlayOnLinux");
        loadFonts();
        ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfiguration.class);

        final MainController mainController = applicationContext.getBean(MainController.class);
        mainController.show();
        mainController.setOnClose(applicationContext::close);
    }

    private void loadFonts() {
        Font.loadFont(getClass().getResource("views/common/mavenpro/MavenPro-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("views/common/roboto/Roboto-Medium.ttf").toExternalForm(), 12);
        Font.loadFont(getClass().getResource("views/common/roboto/Roboto-Light.ttf").toExternalForm(), 12);
    }


}
