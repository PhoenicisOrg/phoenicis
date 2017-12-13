package org.phoenicis.repository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;

import java.util.List;

public interface RepositoryLocationLoader {
    List<RepositoryLocation<? extends Repository>> loadRepositoryLocations();
}
