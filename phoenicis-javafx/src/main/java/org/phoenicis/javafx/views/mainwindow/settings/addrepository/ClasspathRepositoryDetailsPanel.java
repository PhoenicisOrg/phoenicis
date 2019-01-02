package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * A {@link RepositoryDetailsPanel} used to specify the details of a {@link ClasspathRepositoryLocation}.
 *
 * @author marc
 * @since 19.06.17
 */
public class ClasspathRepositoryDetailsPanel extends RepositoryDetailsPanel<ClasspathRepositoryLocation> {
    /**
     * The classpath to the repository location
     */
    private TextField classpathField;

    /**
     * Constructor
     */
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
        content.setId("addClasspathRepository");
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
