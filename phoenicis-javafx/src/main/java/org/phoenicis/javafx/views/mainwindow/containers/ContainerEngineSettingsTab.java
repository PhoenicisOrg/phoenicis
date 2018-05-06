package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * tab to show the engine settings
 */
public class ContainerEngineSettingsTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final ContainerDTO container;

    private final List<Node> lockableElements = new ArrayList<>();

    public ContainerEngineSettingsTab(ContainerDTO container,
            List<EngineSetting> engineSettings) {
        super(tr("Engine Settings"));

        this.container = container;

        this.setClosable(false);

        this.populate(engineSettings);
    }

    private void populate(List<EngineSetting> engineSettings) {
        final VBox displayPane = new VBox();
        final Text title = new TextWithStyle(tr("Display settings"), TITLE_CSS_CLASS);

        displayPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        displayPane.getChildren().add(title);

        final GridPane displayContentPane = new GridPane();
        displayContentPane.getStyleClass().add("grid");

        int row = 0;
        for (EngineSetting setting : engineSettings) {
            final ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setMaxWidth(Double.MAX_VALUE);
            ObservableList<String> items = FXCollections.observableArrayList(setting.getOptions());
            comboBox.setItems(items);
            comboBox.setValue(setting.getCurrentOption(this.container.getName()));
            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> {
                this.lockAll();
                setting.setOption(this.container.getName(), items.indexOf(newValue));
                this.unlockAll();
            }));
            displayContentPane.add(new TextWithStyle(setting.getText(), CAPTION_TITLE_CSS_CLASS), 0, row);
            displayContentPane.add(comboBox, 1, row);
            lockableElements.add(comboBox);
            ++row;
        }

        Region spacer = new Region();
        GridPane.setHgrow(spacer, Priority.ALWAYS);
        displayContentPane.add(spacer, 2, 0);

        displayPane.getChildren().addAll(displayContentPane);
        this.setContent(displayPane);
    }

    public void unlockAll() {
        for (Node element : lockableElements) {
            element.setDisable(false);
        }
    }

    private void lockAll() {
        for (Node element : lockableElements) {
            element.setDisable(true);
        }
    }
}
