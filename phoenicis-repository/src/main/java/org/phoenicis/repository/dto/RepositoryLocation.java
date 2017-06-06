package org.phoenicis.repository.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.phoenicis.tools.files.FileUtilities;

/**
 * Created by marc on 06.06.17.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = LocalRepositoryLocation.class, name = "local"),
        @JsonSubTypes.Type(value = ClasspathRepositoryLocation.class, name = "classpath"),
        @JsonSubTypes.Type(value = GitRepositoryLocation.class, name = "git") })
public abstract class RepositoryLocation<E extends Repository> {
    private final String type;

    protected RepositoryLocation(String type) {
        this.type = type;
    }

    public abstract E createRepository(String cacheDirectoryPath, LocalRepository.Factory localRepositoryFactory,
            ClasspathRepository.Factory classPathRepositoryFactory, FileUtilities fileUtilities);

    public abstract String toDisplayString();

    public String getType() {
        return type;
    }
}
