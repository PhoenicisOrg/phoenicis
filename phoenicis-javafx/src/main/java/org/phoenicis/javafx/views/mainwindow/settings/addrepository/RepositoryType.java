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
    LOCAL {
        @Override
        public String toString() {
            return tr("Local repository");
        }

        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new LocalRepositoryDetailsPanel();
        }
    },

    /**
     * {@link org.phoenicis.repository.location.GitRepositoryLocation} repository type
     */
    GIT {
        @Override
        public String toString() {
            return tr("Git repository");
        }

        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new GitRepositoryDetailsPanel();
        }
    },

    /**
     * {@link org.phoenicis.repository.location.ClasspathRepositoryLocation} repository type
     */
    CLASSPATH {
        @Override
        public String toString() {
            return tr("Classpath repository");
        }

        @Override
        public RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel() {
            return new ClasspathRepositoryDetailsPanel();
        }
    };

    /**
     * Returns the translated name of the repository type
     *
     * @return The translated name of the repository type
     */
    public abstract String toString();

    /**
     * Creates a new {@link RepositoryDetailsPanel} for the repository type
     *
     * @return The created repository details panel
     */
    public abstract RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel();
}
