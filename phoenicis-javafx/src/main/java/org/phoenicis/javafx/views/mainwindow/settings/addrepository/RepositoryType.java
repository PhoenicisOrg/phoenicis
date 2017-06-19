package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 19.06.17.
 */
public enum RepositoryType {
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

    public abstract String toString();

    public abstract RepositoryDetailsPanel<? extends RepositoryLocation<? extends Repository>> getRepositoryDetailsPanel();
}
