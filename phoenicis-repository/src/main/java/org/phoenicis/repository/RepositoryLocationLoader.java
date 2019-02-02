package org.phoenicis.repository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.types.Repository;

import java.util.List;

/**
 * This interface should be implemented to set the behavior of Phoenicis regarding repositories configuration
 */
public interface RepositoryLocationLoader {
    /**
     * Gets the default repository locations
     *
     * @return The default repository locations
     */
    List<RepositoryLocation<? extends Repository>> getDefaultRepositoryLocations();

    /**
     * Loads the repository location
     *
     * @return The loaded repository locations
     * @see RepositoryLocation
     */
    List<RepositoryLocation<? extends Repository>> loadRepositoryLocations();

    /**
     * Save givens repository locations
     *
     * @param repositoryLocations The repository locations
     */
    void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations);
}
