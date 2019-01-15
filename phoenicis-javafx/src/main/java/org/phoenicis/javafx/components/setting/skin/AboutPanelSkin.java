package org.phoenicis.javafx.components.setting.skin;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.setting.control.AboutPanel;
import org.phoenicis.javafx.components.setting.utils.ApplicationBuildInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * The skin for the {@link AboutPanel} component
 */
public class AboutPanelSkin extends SkinBase<AboutPanel, AboutPanelSkin> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AboutPanelSkin.class);

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public AboutPanelSkin(AboutPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final Text title = new Text(tr("About"));
        title.getStyleClass().add("title");

        final GridPane aboutGrid = createAboutGrid();

        final VBox container = new VBox(title, aboutGrid);

        container.getStyleClass().add("containerConfigurationPane");

        getChildren().addAll(container);
    }

    /**
     * Creates the "about" {@link GridPane} containing the application name, version, git revision and build timestamp
     *
     * @return The "about" {@link GridPane} containing the application name, version, git revision and build timestamp
     */
    private GridPane createAboutGrid() {
        final GridPane aboutGrid = new GridPane();
        aboutGrid.getStyleClass().add("grid");
        aboutGrid.setHgap(20);
        aboutGrid.setVgap(10);

        final Text nameDescription = new Text(tr("Name:"));
        nameDescription.getStyleClass().add("captionTitle");

        final Label nameLabel = new Label();
        nameLabel.textProperty().bind(
                Bindings.createStringBinding(() -> Optional.ofNullable(getControl().getBuildInformation())
                        .map(ApplicationBuildInformation::getApplicationName).orElse(null),
                        getControl().buildInformationProperty()));

        aboutGrid.addRow(0, nameDescription, nameLabel);

        final Text versionDescription = new Text(tr("Version:"));
        versionDescription.getStyleClass().add("captionTitle");

        final Label versionLabel = new Label();
        versionLabel.textProperty().bind(
                Bindings.createStringBinding(() -> Optional.ofNullable(getControl().getBuildInformation())
                        .map(ApplicationBuildInformation::getApplicationVersion).orElse(null),
                        getControl().buildInformationProperty()));

        aboutGrid.addRow(1, versionDescription, versionLabel);

        final Text gitRevisionDescription = new Text(tr("Git Revision:"));
        gitRevisionDescription.getStyleClass().add("captionTitle");

        final Hyperlink gitRevisionHyperlink = createGitRevisionHyperlink();

        aboutGrid.addRow(2, gitRevisionDescription, gitRevisionHyperlink);

        final Text buildTimestampDescription = new Text(tr("Build Timestamp:"));
        buildTimestampDescription.getStyleClass().add("captionTitle");

        final Label buildTimestampLabel = new Label();
        buildTimestampLabel.textProperty().bind(
                Bindings.createStringBinding(() -> Optional.ofNullable(getControl().getBuildInformation())
                        .map(ApplicationBuildInformation::getApplicationBuildTimestamp).orElse(null),
                        getControl().buildInformationProperty()));

        aboutGrid.addRow(3, buildTimestampDescription, buildTimestampLabel);

        return aboutGrid;
    }

    /**
     * Creates the {@link Hyperlink} leading to the git revision on github
     *
     * @return The {@link Hyperlink} leading to the git revision on github
     */
    private Hyperlink createGitRevisionHyperlink() {
        final StringBinding revisionText = Bindings.createStringBinding(
                () -> Optional.ofNullable(getControl().getBuildInformation())
                        .map(ApplicationBuildInformation::getApplicationGitRevision).orElse(null),
                getControl().buildInformationProperty());

        final BooleanBinding disableProperty = Bindings.when(Bindings
                .and(Bindings.isNotNull(revisionText), Bindings.notEqual(revisionText, "unknown")))
                .then(false).otherwise(true);

        final Hyperlink gitRevisionHyperlink = new Hyperlink();

        gitRevisionHyperlink.textProperty().bind(revisionText);

        // if the git revision equals "unknown" disable the hyperlink
        gitRevisionHyperlink.disableProperty().bind(disableProperty);
        gitRevisionHyperlink.visitedProperty().bind(disableProperty);
        gitRevisionHyperlink.underlineProperty().bind(disableProperty);

        // if the git revision has been clicked on open the corresponding commit page on github
        gitRevisionHyperlink.setOnAction(event -> {
            try {
                final URI uri = new URI("https://github.com/PhoenicisOrg/phoenicis/commit/"
                        + getControl().getBuildInformation().getApplicationGitRevision());

                getControl().getOpener().open(uri);
            } catch (URISyntaxException e) {
                LOGGER.error("Could not open GitHub URL.", e);
            }
        });

        return gitRevisionHyperlink;
    }
}
