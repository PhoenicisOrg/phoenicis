package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * An enum containing all by the user addable repository types.
 *
 * @author marc
 * @since 19.06.17
 */
public enum RepositoryType {
    /**
     * {@link org.phoenicis.repository.location.LocalRepositoryLocation} repository type
     */
    LOCAL(tr("Local repository")) {
        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new LocalRepositoryDetailsPanel();
        }
    },

    /**
     * {@link org.phoenicis.repository.location.GitRepositoryLocation} repository type
     */
    GIT(tr("Git repository")) {
        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new GitRepositoryDetailsPanel();
        }
    },

    /**
     * {@link org.phoenicis.repository.location.ClasspathRepositoryLocation} repository type
     */
    CLASSPATH(tr("Classpath repository")) {
        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new ClasspathRepositoryDetailsPanel();
        }
    };

    /**
     * The translated name of the repository type
     */
    private String label;

    /**
     * Constructor
     *
     * @param label The translated name of the repository type
     */
    RepositoryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Creates a new {@link RepositoryDetailsPanel} for the repository type
     *
     * @return The created repository details panel
     */
    public abstract RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel();
}
