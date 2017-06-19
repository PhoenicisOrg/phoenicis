package org.phoenicis.javafx.views.mainwindow.settings.addrepository;

import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

/**
 * Created by marc on 19.06.17.
 */
public abstract class RepositoryDetailsPanel<T extends RepositoryLocation<? extends Repository>> extends BorderPane {
    public abstract String getHeader();

    public abstract T createRepositoryLocation();
}
