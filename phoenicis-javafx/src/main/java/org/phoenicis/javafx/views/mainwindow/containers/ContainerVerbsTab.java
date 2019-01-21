package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.VerbsManager;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.ArrayList;
import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * tab to install Verbs for a given container
 */
public class ContainerVerbsTab extends Tab {
    private final ContainerDTO container;
    private final VerbsManager verbsManager;

    private final List<Node> lockableElements = new ArrayList<>();

    public ContainerVerbsTab(ContainerDTO container, VerbsManager verbsManager,
            ApplicationDTO verbs) {
        super(tr("Verbs"));

        this.container = container;
        this.verbsManager = verbsManager;

        this.setClosable(false);

        this.populate(verbs);
    }

    private void populate(ApplicationDTO verbs) {
        final VBox verbsPane = new VBox();
        final Text title = new TextWithStyle(tr("Verbs"), "title");

        verbsPane.getStyleClass().add("containerConfigurationPane");
        verbsPane.getChildren().add(title);

        final TilePane verbsContentPane = new TilePane();
        verbsContentPane.setPrefColumns(3);
        verbsContentPane.getStyleClass().add("grid");

        for (ScriptDTO verb : verbs.getScripts()) {
            Button verbButton = new Button(verb.getScriptName());
            verbButton.getStyleClass().addAll("toolButton");
            verbButton.setStyle("-fx-background-image: url('" + verb.getIcon() + "');");
            verbButton.setOnMouseClicked(event -> {
                this.lockAll();
                // TODO: find a better way to get the engine ID
                this.verbsManager.installVerb(container.getEngine().toLowerCase(), container.getName(), verb.getId(),
                        this::unlockAll, e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withException(e)
                                    .build();

                            errorDialog.showAndWait();
                        }));
            });
            this.lockableElements.add(verbButton);
            verbsContentPane.getChildren().add(verbButton);
        }

        verbsPane.getChildren().add(verbsContentPane);

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // the TilePane adjusts the number of columns
        // already
        verbsContentPane.prefWidthProperty().bind(scrollPane.widthProperty());
        verbsContentPane.prefHeightProperty().bind(scrollPane.heightProperty());
        scrollPane.setBackground(verbsContentPane.getBackground());
        scrollPane.setContent(verbsPane);

        this.setContent(scrollPane);
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
