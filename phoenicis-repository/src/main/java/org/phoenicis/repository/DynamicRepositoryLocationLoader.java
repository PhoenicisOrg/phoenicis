package org.phoenicis.repository;

import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class DynamicRepositoryLocationLoader implements RepositoryLocationLoader {
    private final RepositoryLocationLoader delegated;

    public DynamicRepositoryLocationLoader(ApplicationContext applicationContext, String beanName) {
        this.delegated = applicationContext.getBean(beanName, RepositoryLocationLoader.class);
    }

    @Override
    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations() {
        return delegated.loadRepositoryLocations();
    }
}