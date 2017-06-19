package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 19.06.17.
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

    private Consumer<RepositoryType> onRepositoryTypeSelection;

    public ChooseRepositoryTypePanel() {
        super();

        this.repositoryChoices = FXCollections.observableArrayList(RepositoryType.values());

        this.populate();
    }

    private void populate() {
        choiceBox = new ComboBox<>(repositoryChoices);
        choiceBox.setPromptText(tr("Please select the repository type you want to add"));
        choiceBox.setOnAction(event -> onRepositoryTypeSelection.accept(choiceBox.getSelectionModel().getSelectedItem()));

        Label choiceBoxLabel = new Label(tr("Repository type:"));
        choiceBoxLabel.setLabelFor(choiceBox);

        HBox content = new HBox(choiceBoxLabel, choiceBox);
        HBox.setHgrow(choiceBox, Priority.ALWAYS);

        this.setCenter(content);
    }

    public String getHeader() {
        return tr("Choose the repository type");
    }

    public void setOnRepositoryTypeSelection(Consumer<RepositoryType> onRepositoryTypeSelection) {
        this.onRepositoryTypeSelection = onRepositoryTypeSelection;
    }
}
