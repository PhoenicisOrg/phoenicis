package org.phoenicis.javafx.components.application.skin;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import org.graalvm.polyglot.Value;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.application.control.ApplicationInformationPanel;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.scripts.Installer;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.phoenicis.entities.OperatingSystem;

import java.net.URI;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link ApplicationInformationPanel} component
 */
public class ApplicationInformationPanelSkin
        extends SkinBase<ApplicationInformationPanel, ApplicationInformationPanelSkin> {
    /**
     * The preferred height for the application miniature images
     */
    private final DoubleProperty miniatureHeight;

    /**
     * A list of all scripts for the shown application
     */
    private final ObservableList<ScriptDTO> scripts;

    /**
     * A filtered version of <code>scripts</code>
     */
    private final ObservableList<ScriptDTO> filteredScripts;

    /**
     * A list containing the {@link URI}s for the miniatures for the shown application
     */
    private final ObservableList<URI> miniatureUris;

    /**
     * A mapped list between the <code>miniatureUris</code> and a {@link Region} component
     */
    private final ObservableList<Region> miniatures;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public ApplicationInformationPanelSkin(ApplicationInformationPanel control) {
        super(control);

        this.miniatureHeight = new SimpleDoubleProperty();

        this.scripts = FXCollections.observableArrayList();
        this.miniatureUris = FXCollections.observableArrayList();

        this.filteredScripts = createFilteredScripts();
        this.miniatures = createMiniatures();
    }

    /**
     * Creates a filtered version of <code>scripts</code>
     *
     * @return A filtered version of the scripts list
     */
    private FilteredList<ScriptDTO> createFilteredScripts() {
        final FilteredList<ScriptDTO> filteredScripts = scripts.filtered(getControl()::filterScript);

        filteredScripts.predicateProperty().bind(
                Bindings.createObjectBinding(() -> getControl()::filterScript,
                        getControl().operatingSystemProperty(),
                        getControl().containAllOSCompatibleApplicationsProperty(),
                        getControl().containCommercialApplicationsProperty(),
                        getControl().containRequiresPatchApplicationsProperty(),
                        getControl().containTestingApplicationsProperty()));

        return filteredScripts;
    }

    /**
     * Creates a mapped list between the <code>miniatureUris</code> and a {@link Region} component
     *
     * @return A mapped list between miniatureUris and a Region component
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
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

        final VBox container = new VBox(appDescription, installers, scriptGrid, miniaturesPaneWrapper);

        getChildren().add(container);

        // ensure that the content of the details panel changes when the to be shown application changes
        getControl().applicationProperty().addListener((Observable invalidation) -> updateApplication());
        // initialise the content of the details panel correct
        updateApplication();
    }

    /**
     * Update the content of the details panel when the to be shown application changes
     */
    private void updateApplication() {
        final ApplicationDTO application = getControl().getApplication();

        if (application != null) {
            scripts.setAll(application.getScripts());
            miniatureUris.setAll(application.getMiniatures());
        }
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

            OperatingSystem curOs = new OperatingSystemFetcher().fetchCurrentOperationSystem();
            Label lTesting = new Label();
            if (script.getTestingOperatingSystems().contains(curOs)) {
                lTesting.getStyleClass().add("testingIcon");
                lTesting.setTooltip(new Tooltip(tr("Testing")));
                lTesting.setMinSize(30, 30);
            }
            Label lCommercial = new Label();
            if (!script.isFree()) {
                lCommercial.getStyleClass().add("commercialIcon");
                lCommercial.setTooltip(new Tooltip(tr("Commercial")));
                lCommercial.setMinSize(30, 30);
            }
            Label lPatch = new Label();
            if (script.isRequiresPatch()) {
                lPatch.getStyleClass().add("patchIcon");
                lPatch.setTooltip(new Tooltip(tr("Patch required")));
                lPatch.setMinSize(30, 30);
            }
            Label lOs = new Label();
            if (!script.getCompatibleOperatingSystems().contains(curOs)) {
                lOs.getStyleClass().add("osIcon");
                lOs.setTooltip(new Tooltip(tr("All Operating Systems")));
                lOs.setMinSize(30, 30);
            }
            Label lSpace = new Label();
            lSpace.setPrefSize(30, 30);

            HBox iconBox = new HBox(lTesting, lCommercial, lPatch, lOs, lSpace);

            scriptGrid.addRow(i, scriptName, iconBox, installButton);
        }
    }

    /**
     * Updates the application description when the application changes
     *
     * @param appDescription The web view containing the application description
     */
    private void updateDescription(final WebView appDescription) {
        final ApplicationDTO application = getControl().getApplication();

        if (application != null) {
            appDescription.getEngine().loadContent("<body>" + application.getDescription() + "</body>");
        }
    }

    /**
     * Install the given script
     *
     * @param script The script to be installed
     */
    private void installScript(ScriptDTO script) {
        final StringBuilder executeBuilder = new StringBuilder();
        executeBuilder.append(String.format("TYPE_ID=\"%s\";\n", script.getTypeId()));
        executeBuilder.append(String.format("CATEGORY_ID=\"%s\";\n", script.getCategoryId()));
        executeBuilder.append(String.format("APPLICATION_ID=\"%s\";\n", script.getApplicationId()));
        executeBuilder.append(String.format("SCRIPT_ID=\"%s\";\n", script.getId()));

        executeBuilder.append(script.getScript());
        executeBuilder.append("\n");

        getControl().getScriptInterpreter().createInteractiveSession()
                .eval(executeBuilder.toString(), result -> {
                    Value installer = (Value) result;

                    installer.as(Installer.class).go();
                }, e -> Platform.runLater(() -> {
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
}
