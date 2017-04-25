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

import static org.phoenicis.configuration.localisation.Localisation.translate;

/**
 * This class represents the "About" settings category
 *
 * @author marc
 * @since 23.04.17
 */
public class AboutPanel extends VBox {
    private ApplicationBuildInformation buildInformation;
    private Opener opener;

    private Text title;

    private GridPane aboutGrid;

    private Text nameDescription;
    private Label nameLabel;

    private Text versionDescription;
    private Label versionLabel;

    private Text gitRevisionDescription;
    private Hyperlink gitRevisionHyperlink;

    private Text buildTimestampDescription;
    private Label buildTimestampLabel;

    /**
     * Constructor
     *
     * @param buildInformation The information of the used build of POL 5
     * @param opener           The opener util object to be used to open websites
     */
    public AboutPanel(ApplicationBuildInformation buildInformation, Opener opener) {
        super();

        this.buildInformation = buildInformation;
        this.opener = opener;

        this.getStyleClass().add("containerConfigurationPane");

        this.populate();

        this.getChildren().setAll(title, aboutGrid);
    }

    private void populate() {
        this.title = new TextWithStyle(translate("About"), "title");

        this.aboutGrid = new GridPane();
        this.aboutGrid.getStyleClass().add("grid");
        this.aboutGrid.setHgap(20);
        this.aboutGrid.setVgap(10);

        this.nameDescription = new TextWithStyle(translate("Name:"), "captionTitle");
        this.nameLabel = new Label(buildInformation.getApplicationName());

        this.versionDescription = new TextWithStyle(translate("Version:"), "captionTitle");
        this.versionLabel = new Label(buildInformation.getApplicationVersion());

        this.gitRevisionDescription = new TextWithStyle(translate("Git Revision:"), "captionTitle");
        this.gitRevisionHyperlink = new Hyperlink(buildInformation.getApplicationGitRevision());
        this.gitRevisionHyperlink.setOnAction(event -> {
            try {
                URI uri = new URI("https://github.com/PlayOnLinux/POL-POM-5/commit/" + buildInformation.getApplicationGitRevision());
                opener.open(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        this.buildTimestampDescription = new TextWithStyle(translate("Build Timestamp:"), "captionTitle");
        this.buildTimestampLabel = new Label(buildInformation.getApplicationBuildTimestamp());

        this.aboutGrid.add(nameDescription, 0, 0);
        this.aboutGrid.add(nameLabel, 1, 0);

        this.aboutGrid.add(versionDescription, 0, 1);
        this.aboutGrid.add(versionLabel, 1, 1);

        this.aboutGrid.add(gitRevisionDescription, 0, 2);
        this.aboutGrid.add(gitRevisionHyperlink, 1, 2);

        this.aboutGrid.add(buildTimestampDescription, 0, 3);
        this.aboutGrid.add(buildTimestampLabel, 1, 3);
    }

    /**
     * This class contains information about the POL 5 build
     */
    public static class ApplicationBuildInformation {
        // the name of the application
        private String applicationName;
        // the version of the application (taken from the maven pom file)
        private String applicationVersion;
        // the git revision/commit used to build POL 5
        private String applicationGitRevision;
        // the timestamp when POL 5 was built
        private String applicationBuildTimestamp;

        /**
         * Constructor
         *
         * @param applicationName           the name of the application
         * @param applicationVersion        the version of the application
         * @param applicationGitRevision    the git revision/commit used to build POL 5
         * @param applicationBuildTimestamp the timestamp when POL 5 was built
         */
        public ApplicationBuildInformation(String applicationName, String applicationVersion, String applicationGitRevision, String applicationBuildTimestamp) {
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
