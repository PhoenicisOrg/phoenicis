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

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.phoenicis.javafx.controller.MainController;
import org.phoenicis.multithreading.ControlledThreadPoolExecutorServiceCloser;
import org.phoenicis.repository.RepositoryManager;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class JavaFXApplication extends Application {
    private double splashWidth;
    private double splashHeight;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;

    public static void main(String[] args) {
        try {
            Application.launch(JavaFXApplication.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        final Image splashImage = new Image(getClass().getResourceAsStream("views/common/splash.png"));
        splashWidth = splashImage.getWidth();
        splashHeight = splashImage.getHeight();
        final ImageView splash = new ImageView(splashImage);
        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(splashWidth);
        progressText = new Label(tr("Loading ..."));
        progressText.setFont(new Font(12));
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setEffect(new DropShadow());
    }

    @Override
    public void start(final Stage initStage) {

        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() {
                ObservableList<String> loadedItems = FXCollections.observableArrayList();
                ObservableList<String> requiredFonts = FXCollections.observableArrayList(
                        "views/common/mavenpro/MavenPro-Medium.ttf",
                        "views/common/roboto/Roboto-Medium.ttf",
                        "views/common/roboto/Roboto-Light.ttf",
                        "views/common/roboto/Roboto-Bold.ttf");
                // number of load steps after the fonts have been loaded
                final int numAdditionalLoadSteps = 1;

                updateMessage(tr("Loading fonts ..."));
                for (int i = 0; i < requiredFonts.size(); i++) {
                    Font.loadFont(getClass().getResource(requiredFonts.get(i)).toExternalForm(), 12);
                    updateProgress(i + 1, requiredFonts.size() + numAdditionalLoadSteps);
                    loadedItems.add(requiredFonts.get(i));
                    updateMessage(tr("Loading font {0} of {1} ...", i + 1, requiredFonts.size()));
                }
                updateMessage(tr("All fonts loaded"));

                updateMessage(tr("Loading repository ..."));
                ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                        AppConfigurationNoUi.class);
                RepositoryManager repositoryManager = applicationContext.getBean(RepositoryManager.class);
                repositoryManager.forceSynchronousUpdate();
                updateProgress(requiredFonts.size() + 1, requiredFonts.size() + numAdditionalLoadSteps);
                loadedItems.add("Repository");
                return loadedItems;
            }
        };

        showSplash(initStage, friendTask, this::showMainStage);
        new Thread(friendTask).start();
    }

    private void showMainStage() {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.getIcons().add(new Image(getClass().getResourceAsStream("views/common/phoenicis.png")));
        mainStage.setTitle("Phoenicis");

        ConfigurableApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                AppConfiguration.class);

        final MainController mainController = applicationContext.getBean(MainController.class);
        mainController.show();
        mainController.setOnClose(() -> {
            applicationContext.getBean(ControlledThreadPoolExecutorServiceCloser.class).setCloseImmediately(true);
            applicationContext.close();
        });
        mainStage.toFront();
    }

    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        final Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - splashWidth / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - splashHeight / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    public interface InitCompletionHandler {
        void complete();
    }

}
