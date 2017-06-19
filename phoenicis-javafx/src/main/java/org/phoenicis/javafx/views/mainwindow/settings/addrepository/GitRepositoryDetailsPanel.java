package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.phoenicis.repository.location.GitRepositoryLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 19.06.17.
 */
public class GitRepositoryDetailsPanel extends RepositoryDetailsPanel<GitRepositoryLocation> {
    private final static Logger LOGGER = LoggerFactory.getLogger(GitRepositoryDetailsPanel.class);

    private TextField urlField;

    private TextField branchField;

    public GitRepositoryDetailsPanel() {
        super();

        this.populate();
    }

    /**
     * Populates the repository details step for the git repository
     */
    private void populate() {
        this.urlField = new TextField();
        this.branchField = new TextField("master");

        Label urlLabel = new Label(tr("Git-Url:"));
        urlLabel.setLabelFor(urlField);
        Label branchLabel = new Label(tr("Branch:"));
        branchLabel.setLabelFor(branchField);

        GridPane grid = new GridPane();
        grid.add(urlLabel, 0, 0);
        grid.add(urlField, 1, 0);

        grid.add(branchLabel, 0, 1);
        grid.add(branchField, 1, 1);

        this.setCenter(grid);
    }

    @Override
    public String getHeader() {
        return tr("Choose the location of your git repository");
    }

    @Override
    public GitRepositoryLocation createRepositoryLocation() {
        try {
            return new GitRepositoryLocation.Builder().withGitRepositoryUri(new URL(urlField.getText()).toURI())
                    .withBranch(branchField.getText()).build();
        } catch (MalformedURLException | URISyntaxException e) {
            LOGGER.error(String.format("The given url '%s' is no valid URI or URL", urlField.getText()), e);
            return null;
        }
    }
}
