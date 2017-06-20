package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A panel used to provide the user a component to choose the repository type he/she wants to add.
 *
 * @author marc
 * @since 19.06.17
 */
public class ChooseRepositoryTypePanel extends BorderPane {
    /**
     * A list containing all possible repository types to be added with this dialog
     */
    private ObservableList<RepositoryType> repositoryChoices;

    /**
     * The choice box containing, which repository type the user wants to add
     */
    private ComboBox<RepositoryType> choiceBox;

    /**
     * A consumer to be called, when the user selects a repository type
     */
    private Consumer<RepositoryType> onRepositoryTypeSelection;

    /**
     * Constructor
     */
    public ChooseRepositoryTypePanel() {
        super();

        this.repositoryChoices = FXCollections.observableArrayList(RepositoryType.values());

        this.populate();
    }

    /**
     * Populates the content of this component
     */
    private void populate() {
        choiceBox = new ComboBox<>(repositoryChoices);
        choiceBox.setPromptText(tr("Please select the repository type you want to add"));

        choiceBox.setConverter(new StringConverter<RepositoryType>() {
            @Override
            public String toString(RepositoryType repositoryType) {
                return repositoryType.getLabel();
            }

            @Override
            public RepositoryType fromString(String string) {
                return Arrays.stream(RepositoryType.values()).filter(type -> type.getLabel().equals(string)).findAny()
                        .orElse(null);
            }
        });

        choiceBox.setOnAction(
                event -> onRepositoryTypeSelection.accept(choiceBox.getSelectionModel().getSelectedItem()));

        Label choiceBoxLabel = new Label(tr("Repository type:"));
        choiceBoxLabel.setLabelFor(choiceBox);

        HBox content = new HBox(choiceBoxLabel, choiceBox);
        HBox.setHgrow(choiceBox, Priority.ALWAYS);

        this.setCenter(content);
    }

    /**
     * Returns the translated header text for this panel
     *
     * @return The translated header text
     */
    public String getHeader() {
        return tr("Choose the repository type");
    }

    /**
     * Updates the consumer, which is called after the user has selected a repository type
     *
     * @param onRepositoryTypeSelection The new consumer
     */
    public void setOnRepositoryTypeSelection(Consumer<RepositoryType> onRepositoryTypeSelection) {
        this.onRepositoryTypeSelection = onRepositoryTypeSelection;
    }
}
