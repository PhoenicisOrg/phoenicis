package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.javafx.views.common.TextWithStyle;

import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 27.05.17.
 */
public class ContainerInformationTab extends Tab {
    private static final String CAPTION_TITLE_CSS_CLASS = "captionTitle";
    private static final String CONFIGURATION_PANE_CSS_CLASS = "containerConfigurationPane";
    private static final String TITLE_CSS_CLASS = "title";

    private final WinePrefixContainerDTO container;

    private Consumer<ContainerDTO> onDeletePrefix;

    public ContainerInformationTab(WinePrefixContainerDTO container) {
        super(tr("Information"));

        this.container = container;

        this.setClosable(false);

        this.populate();
    }

    private void populate() {
        final VBox informationPane = new VBox();
        final Text title = new TextWithStyle(tr("Information"), TITLE_CSS_CLASS);

        informationPane.getStyleClass().add(CONFIGURATION_PANE_CSS_CLASS);
        informationPane.getChildren().add(title);

        final GridPane informationContentPane = new GridPane();
        informationContentPane.getStyleClass().add("grid");

        informationContentPane.add(new TextWithStyle(tr("Name:"), CAPTION_TITLE_CSS_CLASS), 0, 0);
        Label name = new Label(container.getName());
        name.setWrapText(true);
        informationContentPane.add(name, 1, 0);

        informationContentPane.add(new TextWithStyle(tr("Path:"), CAPTION_TITLE_CSS_CLASS), 0, 1);
        Label path = new Label(container.getPath());
        path.setWrapText(true);
        informationContentPane.add(path, 1, 1);

        informationContentPane.add(new TextWithStyle(tr("Installed shortcuts:"), CAPTION_TITLE_CSS_CLASS), 0, 2);
        Label installedShortcuts = new Label(container.getInstalledShortcuts().stream()
                .map(shortcutDTO -> shortcutDTO.getInfo().getName()).collect(Collectors.joining("; ")));
        installedShortcuts.setWrapText(true);
        informationContentPane.add(installedShortcuts, 1, 2);

        // TODO: find generic solution which works for all container types
        informationContentPane.add(new TextWithStyle(tr("Wine version:"), CAPTION_TITLE_CSS_CLASS), 0, 3);
        Label version = new Label(container.getVersion());
        version.setWrapText(true);
        informationContentPane.add(version, 1, 3);

        informationContentPane.add(new TextWithStyle(tr("Wine architecture:"), CAPTION_TITLE_CSS_CLASS), 0, 4);
        Label architecture = new Label(container.getArchitecture());
        architecture.setWrapText(true);
        informationContentPane.add(architecture, 1, 4);

        informationContentPane.add(new TextWithStyle(tr("Wine distribution:"), CAPTION_TITLE_CSS_CLASS), 0, 5);
        Label distribution = new Label(container.getDistribution());
        distribution.setWrapText(true);
        informationContentPane.add(distribution, 1, 5);

        Region spacer = new Region();
        spacer.setPrefHeight(20);
        VBox.setVgrow(spacer, Priority.NEVER);

        // changing engine does not work currently
        // disabled combobox to avoid confusion of users
        /*
         * ComboBox<EngineVersionDTO> changeEngineComboBox = new ComboBox<EngineVersionDTO>(
         * FXCollections.observableList(engineVersions));
         * changeEngineComboBox.setConverter(new StringConverter<EngineVersionDTO>() {
         * 
         * @Override
         * public String toString(EngineVersionDTO object) {
         * return object.getVersion();
         * }
         * 
         * @Override
         * public EngineVersionDTO fromString(String string) {
         * return engineVersions.stream().filter(engineVersion -> engineVersion.getVersion().equals(string))
         * .findFirst().get();
         * }
         * });
         * changeEngineComboBox.getSelectionModel().select(engineVersions.stream()
         * .filter(engineVersion -> engineVersion.getVersion().equals(container.getVersion())).findFirst().get());
         */

        Button deleteButton = new Button(tr("Delete container"));
        deleteButton.setOnMouseClicked(event -> this.onDeletePrefix.accept(container));

        informationPane.getChildren().addAll(informationContentPane, spacer, /* changeEngineComboBox, */ deleteButton);
        this.setContent(informationPane);
    }

    public void setOnDeletePrefix(Consumer<ContainerDTO> onDeletePrefix) {
        this.onDeletePrefix = onDeletePrefix;
    }
}
