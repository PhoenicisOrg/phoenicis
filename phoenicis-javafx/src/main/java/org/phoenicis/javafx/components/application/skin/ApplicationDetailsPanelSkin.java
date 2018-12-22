package org.phoenicis.javafx.components.application.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.application.control.ApplicationDetailsPanel;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.net.URI;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ApplicationDetailsPanelSkin extends SkinBase<ApplicationDetailsPanel, ApplicationDetailsPanelSkin> {
    private final StringProperty title;

    private final DoubleProperty miniatureHeight;

    private final ObservableList<ScriptDTO> scripts;

    private final ObservableList<ScriptDTO> filteredScripts;

    private final ObservableList<URI> miniatureUris;

    private final ObservableList<Region> miniatures;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationDetailsPanelSkin(ApplicationDetailsPanel control) {
        super(control);

        this.title = new SimpleStringProperty();
        this.miniatureHeight = new SimpleDoubleProperty();

        this.scripts = FXCollections.observableArrayList();
        this.miniatureUris = FXCollections.observableArrayList();

        this.filteredScripts = createFilteredScripts();
        this.miniatures = createMiniatures();
    }

    private FilteredList<ScriptDTO> createFilteredScripts() {
        final ApplicationFilter filter = getControl().getFilter();

        final FilteredList<ScriptDTO> filteredScripts = scripts.filtered(filter::filter);

        filteredScripts.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter::filter,
                        filter.containAllOSCompatibleApplicationsProperty(),
                        filter.containCommercialApplicationsProperty(),
                        filter.containRequiresPatchApplicationsProperty(),
                        filter.containTestingApplicationsProperty()));

        return filteredScripts;
    }

    private ObservableList<Region> createMiniatures() {
        return new MappedList<>(miniatureUris, miniatureUri -> {
            final Region image = new Region();
            image.getStyleClass().add("appMiniature");
            image.setStyle(String.format("-fx-background-image: url(\"%s\");", miniatureUri.toString()));

            image.prefHeightProperty().bind(miniatureHeight);
            image.prefWidthProperty().bind(miniatureHeight.multiply(1.5));

            return image;
        });
    }

    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("detailsPane");

        container.setTop(createHeader());
        container.setCenter(createContent());

        getControl().applicationProperty().addListener((Observable invalidation) -> updateApplication());
        updateApplication();

        getChildren().addAll(container);
    }

    private void updateApplication() {
        final ApplicationDTO application = getControl().getApplication();

        if (application != null) {
            title.setValue(application.getName());
            scripts.setAll(application.getScripts());
            miniatureUris.setAll(application.getMiniatures());
        }
    }

    private VBox createContent() {
        final WebView appDescription = new WebView();
        appDescription.getEngine().userStyleSheetLocationProperty().bind(getControl().webEngineStylesheetProperty());
        VBox.setVgrow(appDescription, Priority.ALWAYS);

        getControl().applicationProperty().addListener((Observable invalidation) -> updateDescription(appDescription));
        updateDescription(appDescription);

        final Label installers = new Label(tr("Installers"));
        installers.getStyleClass().add("descriptionTitle");

        final GridPane scriptGrid = new GridPane();

        filteredScripts.addListener((InvalidationListener) change -> updateScripts(scriptGrid));
        getControl().showScriptSourceProperty().addListener((Observable invalidation) -> updateScripts(scriptGrid));
        updateScripts(scriptGrid);

        final HBox miniaturesPane = new HBox();
        miniaturesPane.getStyleClass().add("appPanelMiniaturesPane");

        Bindings.bindContent(miniaturesPane.getChildren(), miniatures);

        final ScrollPane miniaturesPaneWrapper = new ScrollPane(miniaturesPane);
        miniaturesPaneWrapper.getStyleClass().add("appPanelMiniaturesPaneWrapper");

        miniatureHeight.bind(miniaturesPaneWrapper.heightProperty().multiply(0.8));

        return new VBox(appDescription, installers, scriptGrid, miniaturesPaneWrapper);
    }

    /**
     * Refreshes the shown scripts.
     * When this method is called it begins by clearing the <code>scriptGrid</code>.
     * Afterwards this method refills it.
     */
    private void updateScripts(final GridPane scriptGrid) {
        scriptGrid.getChildren().clear();

        for (int i = 0; i < filteredScripts.size(); i++) {
            ScriptDTO script = filteredScripts.get(i);

            final Label scriptName = new Label(script.getScriptName());
            GridPane.setHgrow(scriptName, Priority.ALWAYS);

            if (getControl().isShowScriptSource()) {
                final Tooltip tooltip = new Tooltip(tr("Source: {0}", script.getScriptSource()));
                Tooltip.install(scriptName, tooltip);
            }

            final Button installButton = new Button(tr("Install"));
            installButton.setOnMouseClicked(evt -> {
                try {
                    installScript(script);
                } catch (IllegalArgumentException e) {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Error while trying to download the installer"))
                            .withException(e)
                            .build();

                    errorDialog.showAndWait();
                }
            });

            scriptGrid.addRow(i, scriptName, installButton);
        }
    }

    private void updateDescription(final WebView appDescription) {
        final ApplicationDTO application = getControl().getApplication();

        if (application != null) {
            appDescription.getEngine().loadContent("<body>" + application.getDescription() + "</body>");
        }
    }

    private HBox createHeader() {
        final Label titleLabel = new Label();
        titleLabel.getStyleClass().add("descriptionTitle");
        titleLabel.textProperty().bind(title);

        final Button closeButton = new Button();
        closeButton.getStyleClass().add("closeIcon");
        closeButton.setOnAction(event -> getOnClose().ifPresent(Runnable::run));

        final Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);

        return new HBox(titleLabel, filler, closeButton);
    }

    private void installScript(ScriptDTO script) {
        final StringBuilder executeBuilder = new StringBuilder();
        executeBuilder.append(String.format("TYPE_ID=\"%s\";\n", script.getTypeId()));
        executeBuilder.append(String.format("CATEGORY_ID=\"%s\";\n", script.getCategoryId()));
        executeBuilder.append(String.format("APPLICATION_ID=\"%s\";\n", script.getApplicationId()));
        executeBuilder.append(String.format("SCRIPT_ID=\"%s\";\n", script.getId()));

        executeBuilder.append(script.getScript());
        executeBuilder.append("\n");

        // TODO: use Java interface instead of String
        executeBuilder.append("new Installer().run();");

        getControl().getScriptInterpreter().runScript(executeBuilder.toString(), e -> Platform.runLater(() -> {
            // no exception if installation is cancelled
            if (!(e.getCause() instanceof InterruptedException)) {
                final ErrorDialog errorDialog = ErrorDialog.builder()
                        .withMessage(tr("The script ended unexpectedly"))
                        .withException(e)
                        .build();

                errorDialog.showAndWait();
            }
        }));
    }

    private Optional<Runnable> getOnClose() {
        return Optional.ofNullable(getControl().getOnClose());
    }
}
