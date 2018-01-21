package org.phoenicis.repository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import java.util.List;

/**
 * This interface should be implemented to set the behavior of Phoenicis regarding repositories configuration
 */
public interface RepositoryLocationLoader {
    /**
     * Loads the repository location
     * @see RepositoryLocation
     * @return The loaded repository locations
     */
    List<RepositoryLocation<? extends Repository>> loadRepositoryLocations();

    /**
     * Save givens repository locations
     * @param repositoryLocations The repository locations
     */
    void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations);
}
