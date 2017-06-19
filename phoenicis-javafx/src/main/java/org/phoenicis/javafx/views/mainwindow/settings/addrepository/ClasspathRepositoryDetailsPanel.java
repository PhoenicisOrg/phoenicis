package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;

import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 19.06.17.
 */
public class ClasspathRepositoryDetailsPanel extends RepositoryDetailsPanel<ClasspathRepositoryLocation> {
    private TextField classpathField;

    public ClasspathRepositoryDetailsPanel() {
        super();

        this.populate();
    }

    /**
     * Populates the repository details step for the classpath repository
     */
    private void populate() {
        this.classpathField = new TextField();

        Label classpathLabel = new Label(tr("Classpath:"));
        classpathLabel.setLabelFor(classpathField);

        HBox content = new HBox(classpathLabel, classpathField);
        HBox.setHgrow(classpathField, Priority.ALWAYS);

        this.setCenter(content);
    }

    @Override
    public String getHeader() {
        return tr("Choose the location of your classpath repository");
    }

    @Override
    public ClasspathRepositoryLocation createRepositoryLocation() {
        return new ClasspathRepositoryLocation(classpathField.getText());
    }
}
