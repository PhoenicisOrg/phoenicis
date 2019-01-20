package org.phoenicis.javafx.components.library.skin;

import javafx.css.PseudoClass;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;
import org.phoenicis.javafx.components.library.control.ShortcutCreationDetailsPanel;
import org.phoenicis.javafx.views.common.ColumnConstraintsWithPercentage;
import org.phoenicis.library.dto.ShortcutCreationDTO;

import java.io.File;
import java.net.URI;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link ShortcutCreationDetailsPanel} component
 */
public class ShortcutCreationDetailsPanelSkin
        extends DetailsPanelBaseSkin<ShortcutCreationDetailsPanel, ShortcutCreationDetailsPanelSkin> {
    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ShortcutCreationDetailsPanelSkin(ShortcutCreationDetailsPanel control) {
        super(control);

        this.title.setValue(tr("Create a new shortcut"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Node createContent() {
        final GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("shortcut-information-grid");

        ColumnConstraints labelConstraints = new ColumnConstraintsWithPercentage(30);
        ColumnConstraints valueConstraints = new ColumnConstraintsWithPercentage(70);

        gridPane.getColumnConstraints().addAll(labelConstraints, valueConstraints);

        // add name input
        final TextField name = addName(gridPane);

        final Tooltip nameErrorTooltip = new Tooltip(tr("Please specify a name!"));

        // add category input
        final TextField category = addCategory(gridPane);

        final Tooltip categoryErrorTooltip = new Tooltip(tr("Please specify a category!"));

        // add description input
        final TextArea description = addDescription(gridPane);

        // add miniature input
        final TextField miniature = addMiniature(gridPane);

        final Tooltip miniatureErrorTooltip = new Tooltip(tr("Please specify a valid miniature!"));

        // add executable input
        final TextField executable = addExecutable(gridPane);

        final Tooltip executableErrorTooltip = new Tooltip(tr("Please specify a valid executable!"));

        // add create button
        final Button createButton = new Button(tr("Create"));
        createButton.setOnMouseClicked(event -> {
            boolean error = false;

            if (StringUtils.isEmpty(name.getText())) {
                name.pseudoClassStateChanged(errorClass, true);
                name.setTooltip(nameErrorTooltip);
                error = true;
            }

            if (StringUtils.isEmpty(category.getText())) {
                category.pseudoClassStateChanged(errorClass, true);
                category.setTooltip(categoryErrorTooltip);
                error = true;
            }

            URI miniatureUri = null;
            // miniature null is ok (will use default)
            // but if a miniature is given, it must exist
            if (StringUtils.isNotEmpty(miniature.getText())) {
                File miniatureFile = new File(miniature.getText());
                if (miniatureFile.exists()) {
                    miniatureUri = miniatureFile.toURI();
                } else {
                    miniature.pseudoClassStateChanged(errorClass, true);
                    miniature.setTooltip(miniatureErrorTooltip);
                    error = true;
                }
            }

            File executableFile = new File(executable.getText());
            if (!executableFile.exists()) {
                executable.pseudoClassStateChanged(errorClass, true);
                executable.setTooltip(executableErrorTooltip);
                error = true;
            }

            if (!error) {
                final ShortcutCreationDTO newShortcut = new ShortcutCreationDTO.Builder()
                        .withName(name.getText())
                        .withCategory(category.getText())
                        .withDescription(description.getText())
                        .withMiniature(miniatureUri)
                        .withExecutable(executableFile).build();

                getControl().getOnCreateShortcut().accept(newShortcut);
            }
        });

        final VBox container = new VBox(gridPane, createButton);
        container.getStyleClass().addAll("library-details-panel-content", "create-shortcut-panel-content");

        return container;
    }

    /**
     * Adds the shortcut name input to the given {@link GridPane}
     *
     * @param gridPane The grid pane to which the shortcut name input should be added
     * @return The {@link TextField} containing the shortcut name
     */
    private TextField addName(final GridPane gridPane) {
        final int row = gridPane.getRowCount();

        final Label nameLabel = new Label(tr("Name:"));
        nameLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(nameLabel, VPos.TOP);

        final TextField name = new TextField();

        gridPane.addRow(row, nameLabel, name);

        return name;
    }

    /**
     * Adds the category input to the given {@link GridPane}
     *
     * @param gridPane The grid pane to which the category input should be added
     * @return The {@link TextField} containing the category
     */
    private TextField addCategory(final GridPane gridPane) {
        final int row = gridPane.getRowCount();

        final Label categoryLabel = new Label(tr("Category:"));
        categoryLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(categoryLabel, VPos.TOP);

        final TextField category = new TextField();

        gridPane.addRow(row, categoryLabel, category);

        return category;
    }

    /**
     * Adds the description input to the given {@link GridPane}
     *
     * @param gridPane The grid pane to which the description input should be added
     * @return The {@link TextArea} containing the description
     */
    private TextArea addDescription(final GridPane gridPane) {
        final int row = gridPane.getRowCount();

        final Label descriptionLabel = new Label(tr("Description:"));
        descriptionLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(descriptionLabel, VPos.TOP);

        final TextArea description = new TextArea();

        gridPane.addRow(row, descriptionLabel, description);

        return description;
    }

    /**
     * Adds the miniature selection input to the given {@link GridPane}
     *
     * @param gridPane The grid pane to which the miniature selection should be added
     * @return The {@link TextField} containing the miniature input
     */
    private TextField addMiniature(final GridPane gridPane) {
        final int row = gridPane.getRowCount();

        final Label miniatureLabel = new Label(tr("Miniature:"));
        miniatureLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(miniatureLabel, VPos.TOP);

        final TextField miniature = new TextField();

        final Button openMiniatureBrowser = new Button(tr("Browse..."));
        openMiniatureBrowser.setOnAction(event -> {
            FileChooser chooser = new FileChooser();

            // open in containers directory if it exists
            File containersDir = new File(getControl().getContainersPath());
            if (containersDir.canRead()) {
                chooser.setInitialDirectory(containersDir);
            }

            chooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter(tr("Images"), "*.miniature, *.png"));

            File newMiniature = chooser.showOpenDialog(getControl().getScene().getWindow());
            if (newMiniature != null) {
                miniature.setText(newMiniature.toString());
            }
        });

        final HBox miniatureHbox = new HBox(miniature, openMiniatureBrowser);
        miniatureHbox.getStyleClass().add("file-selection-container");

        HBox.setHgrow(miniature, Priority.ALWAYS);

        gridPane.addRow(row, miniatureLabel, miniatureHbox);

        return miniature;
    }

    /**
     * Adds the executable selection input to the given {@link GridPane}
     *
     * @param gridPane The grid pane to which the executable selection should be added
     * @return The {@link TextField} containing the executable input
     */
    private TextField addExecutable(final GridPane gridPane) {
        final int row = gridPane.getRowCount();

        final Label executableLabel = new Label(tr("Executable:"));
        executableLabel.getStyleClass().add("captionTitle");

        GridPane.setValignment(executableLabel, VPos.TOP);

        final TextField executable = new TextField();

        final Button openExecutableBrowser = new Button(tr("Browse..."));
        openExecutableBrowser.setOnAction(event -> {
            FileChooser chooser = new FileChooser();

            // open in containers directory if it exists
            File containersDir = new File(getControl().getContainersPath());
            if (containersDir.canRead()) {
                chooser.setInitialDirectory(containersDir);
            }

            File newExecutable = chooser.showOpenDialog(getControl().getScene().getWindow());
            if (newExecutable != null) {
                executable.setText(newExecutable.toString());
            }
        });

        final HBox executableHbox = new HBox(executable, openExecutableBrowser);
        executableHbox.getStyleClass().add("file-selection-container");

        HBox.setHgrow(executable, Priority.ALWAYS);

        gridPane.addRow(row, executableLabel, executableHbox);

        return executable;
    }
}
