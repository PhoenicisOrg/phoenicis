package org.phoenicis.javafx.components.setting.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.RepositoriesPanelSkin;
import org.phoenicis.repository.RepositoryLocationLoader;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

public class RepositoriesPanel extends ControlBase<RepositoriesPanel, RepositoriesPanelSkin> {
    private final RepositoryLocationLoader repositoryLocationLoader;

    private final ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations;

    private final ObjectProperty<Runnable> onRepositoryRefresh;

    public RepositoriesPanel(RepositoryLocationLoader repositoryLocationLoader,
            ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations,
            ObjectProperty<Runnable> onRepositoryRefresh) {
        super();

        this.repositoryLocationLoader = repositoryLocationLoader;
        this.repositoryLocations = repositoryLocations;
        this.onRepositoryRefresh = onRepositoryRefresh;
    }

    public RepositoriesPanel(RepositoryLocationLoader repositoryLocationLoader,
            ObservableList<RepositoryLocation<? extends Repository>> repositoryLocations) {
        this(repositoryLocationLoader, repositoryLocations, new SimpleObjectProperty<>());
    }

    @Override
    public RepositoriesPanelSkin createSkin() {
        return new RepositoriesPanelSkin(this);
    }

    public RepositoryLocationLoader getRepositoryLocationLoader() {
        return repositoryLocationLoader;
    }

    public ObservableList<RepositoryLocation<? extends Repository>> getRepositoryLocations() {
        return repositoryLocations;
    }

    public Runnable getOnRepositoryRefresh() {
        return onRepositoryRefresh.get();
    }

    public ObjectProperty<Runnable> onRepositoryRefreshProperty() {
        return onRepositoryRefresh;
    }

    public void setOnRepositoryRefresh(Runnable onRepositoryRefresh) {
        this.onRepositoryRefresh.set(onRepositoryRefresh);
    }
}
