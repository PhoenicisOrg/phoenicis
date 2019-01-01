package org.phoenicis.javafx.views.mainwindow.settings;

import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.phoenicis.javafx.views.common.TextWithStyle;
import org.phoenicis.tools.system.opener.Opener;

import java.net.URI;
import java.net.URISyntaxException;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * This class represents the "About" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class AboutPanel extends VBox {

    /**
     * Constructor
     *
     * @param buildInformation The information of the used build of Phoenicis
     * @param opener The opener util object to be used to open websites
     */
    public AboutPanel(ApplicationBuildInformation buildInformation, Opener opener) {
        super();

        this.getStyleClass().add("containerConfigurationPane");

        this.populate(buildInformation, opener);
    }

    private void populate(ApplicationBuildInformation buildInformation, Opener opener) {
        Text title = new TextWithStyle(tr("About"), "title");

        GridPane aboutGrid = new GridPane();
        aboutGrid.getStyleClass().add("grid");
        aboutGrid.setHgap(20);
        aboutGrid.setVgap(10);

        final Text nameDescription = new TextWithStyle(tr("Name:"), "captionTitle");
        final Label nameLabel = new Label(buildInformation.getApplicationName());
        aboutGrid.add(nameDescription, 0, 0);
        aboutGrid.add(nameLabel, 1, 0);

        final Text versionDescription = new TextWithStyle(tr("Version:"), "captionTitle");
        final Label versionLabel = new Label(buildInformation.getApplicationVersion());
        aboutGrid.add(versionDescription, 0, 1);
        aboutGrid.add(versionLabel, 1, 1);

        final Text gitRevisionDescription = new TextWithStyle(tr("Git Revision:"), "captionTitle");

        final String gitRevision = buildInformation.getApplicationGitRevision();
        aboutGrid.add(gitRevisionDescription, 0, 2);
        if ("unknown".equals(gitRevision)) {
            final Label gitRevisionLabel = new Label(gitRevision);
            aboutGrid.add(gitRevisionLabel, 1, 2);
        } else {
            final Hyperlink gitRevisionHyperlink = new Hyperlink(gitRevision);
            gitRevisionHyperlink.setOnAction(event -> {
                try {
                    URI uri = new URI("https://github.com/PhoenicisOrg/phoenicis/commit/"
                            + buildInformation.getApplicationGitRevision());
                    opener.open(uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            });
            aboutGrid.add(gitRevisionHyperlink, 1, 2);
        }

        final Text buildTimestampDescription = new TextWithStyle(tr("Build Timestamp:"), "captionTitle");
        final Label buildTimestampLabel = new Label(buildInformation.getApplicationBuildTimestamp());
        aboutGrid.add(buildTimestampDescription, 0, 3);
        aboutGrid.add(buildTimestampLabel, 1, 3);

        this.getChildren().setAll(title, aboutGrid);
    }

    /**
     * This class contains information about the Phoenicis build
     */
    public static class ApplicationBuildInformation {
        // the name of the application
        private String applicationName;
        // the version of the application (taken from the maven pom file)
        private String applicationVersion;
        // the git revision/commit used to build Phoenicis
        private String applicationGitRevision;
        // the timestamp when Phoenicis was built
        private String applicationBuildTimestamp;

        /**
         * Constructor
         *
         * @param applicationName the name of the application
         * @param applicationVersion the version of the application
         * @param applicationGitRevision the git revision/commit used to build Phoenicis
         * @param applicationBuildTimestamp the timestamp when Phoenicis was built
         */
        public ApplicationBuildInformation(String applicationName, String applicationVersion,
                String applicationGitRevision, String applicationBuildTimestamp) {
            this.applicationName = applicationName;
            this.applicationVersion = applicationVersion;
            this.applicationGitRevision = applicationGitRevision;
            this.applicationBuildTimestamp = applicationBuildTimestamp;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public String getApplicationVersion() {
            return applicationVersion;
        }

        public String getApplicationGitRevision() {
            return applicationGitRevision;
        }

        public String getApplicationBuildTimestamp() {
            return applicationBuildTimestamp;
        }
    }
}
