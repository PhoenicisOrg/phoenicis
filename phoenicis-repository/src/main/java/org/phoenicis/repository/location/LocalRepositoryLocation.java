package org.phoenicis.repository.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.tools.files.FileUtilities;

import java.io.File;

/**
 * Location information for a repository located inside the local file system
 *
 * @author marc
 * @since 06.06.17
 */
@JsonDeserialize
public class LocalRepositoryLocation extends RepositoryLocation<LocalRepository> {
    /**
     * The path to the repository folder
     */
    private final File repositoryLocation;

    /**
     * Constructor
     *
     * @param repositoryLocation The path to the folder containing the repository
     */
    @JsonCreator
    public LocalRepositoryLocation(@JsonProperty("repositoryLocation") File repositoryLocation) {
        super("local");

        this.repositoryLocation = repositoryLocation;
    }

    @Override
    public LocalRepository createRepository(String cacheDirectoryPath, LocalRepository.Factory localRepositoryFactory,
            ClasspathRepository.Factory classPathRepositoryFactory, FileUtilities fileUtilities) {
        return localRepositoryFactory.createInstance(repositoryLocation);
    }

    public File getRepositoryLocation() {
        return repositoryLocation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalRepositoryLocation that = (LocalRepositoryLocation) o;

        return new EqualsBuilder()
                .append(repositoryLocation, that.repositoryLocation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(repositoryLocation)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(repositoryLocation).toString();
    }

    @Override
    public String toDisplayString() {
        return String.format("file:%s", repositoryLocation.toString());
    }
}
