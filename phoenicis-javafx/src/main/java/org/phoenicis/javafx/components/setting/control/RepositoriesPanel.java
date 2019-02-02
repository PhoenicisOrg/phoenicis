package org.phoenicis.javafx.components.setting.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.RepositoriesPanelSkin;
import org.phoenicis.repository.RepositoryLocationLoader;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

/**
 * A component class used to show and manipulate all registered repositories with Phoenicis
 */
public class RepositoriesPanel extends ControlBase<RepositoriesPanel, RepositoriesPanelSkin> {
    /**
     * The repository location loader
     */
    private final ObjectProperty<RepositoryLocationLoader> repositoryLocationLoader;

    /**
     * A list containing all known/registered repository locations
     */
    private final ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations;

    /**
     * The callback for when the repositories should be refreshed
     */
    private final ObjectProperty<Runnable> onRepositoryRefresh;

    /**
     * Constructor
     *
     * @param repositoryLocationLoader The repository location loader
     * @param repositoryLocations A list containing all known/registered repository locations
     * @param onRepositoryRefresh The callback for when the repositories should be refreshed
     */
    public RepositoriesPanel(ObjectProperty<RepositoryLocationLoader> repositoryLocationLoader,
            ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations,
            ObjectProperty<Runnable> onRepositoryRefresh) {
        super();

        this.repositoryLocationLoader = repositoryLocationLoader;
        this.repositoryLocations = repositoryLocations;
        this.onRepositoryRefresh = onRepositoryRefresh;
    }

    /**
     * Constructor
     *
     * @param repositoryLocations A list containing all known/registered repository locations
     */
    public RepositoriesPanel(ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations) {
        this(new SimpleObjectProperty<>(), repositoryLocations, new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RepositoriesPanelSkin createSkin() {
        return new RepositoriesPanelSkin(this);
    }

    public RepositoryLocationLoader getRepositoryLocationLoader() {
        return this.repositoryLocationLoader.get();
    }

    public ObjectProperty<RepositoryLocationLoader> repositoryLocationLoaderProperty() {
        return this.repositoryLocationLoader;
    }

    public void setRepositoryLocationLoader(RepositoryLocationLoader repositoryLocationLoader) {
        this.repositoryLocationLoader.set(repositoryLocationLoader);
    }

    public ObservableList<RepositoryLocation<? extends Repository>> getRepositoryLocations() {
        return this.repositoryLocations;
    }

    public Runnable getOnRepositoryRefresh() {
        return this.onRepositoryRefresh.get();
    }

    public ObjectProperty<Runnable> onRepositoryRefreshProperty() {
        return this.onRepositoryRefresh;
    }

    public void setOnRepositoryRefresh(Runnable onRepositoryRefresh) {
        this.onRepositoryRefresh.set(onRepositoryRefresh);
    }
}
