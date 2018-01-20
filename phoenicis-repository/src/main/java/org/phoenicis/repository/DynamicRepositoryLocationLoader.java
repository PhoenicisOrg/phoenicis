package org.phoenicis.repository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class DynamicRepositoryLocationLoader implements RepositoryLocationLoader {
    private final RepositoryLocationLoader delegated;

    public DynamicRepositoryLocationLoader(ApplicationContext applicationContext, String beanName) {
        this(applicationContext.getBean(beanName, RepositoryLocationLoader.class));
    }

    public DynamicRepositoryLocationLoader(RepositoryLocationLoader repositoryLocationLoader) {
        this.delegated = repositoryLocationLoader;
    }

    @Override
    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations() {
        return delegated.loadRepositoryLocations();
    }

    @Override
    public void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations) {
        delegated.saveRepositories(repositoryLocations);
    }
}