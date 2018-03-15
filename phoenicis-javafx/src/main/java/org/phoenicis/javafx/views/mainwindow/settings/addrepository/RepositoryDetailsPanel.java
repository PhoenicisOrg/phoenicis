package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.layout.BorderPane;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

/**
 * A repository details panel used to add specific repository type dependent information.
 *
 * @param <T> The specific repository type catered by this details panel
 * @author marc
 * @since 19.06.17
 */
public abstract class RepositoryDetailsPanel<T extends RepositoryLocation<? extends Repository>> extends BorderPane {
    /**
     * Returns the translated header text for this details panel
     *
     * @return The translated header text
     */
    public abstract String getHeader();

    /**
     * Returns a new instance of a {@link RepositoryLocation} of type {@link T}
     * containing the repository specific details specified by the user
     *
     * @return A new instance of a {@link RepositoryLocation}
     */
    public abstract T createRepositoryLocation();
}
