package org.phoenicis.repository.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.tools.files.FileUtilities;

import java.io.File;

/**
 * Created by marc on 06.06.17.
 */
@JsonDeserialize(builder = LocalRepositoryLocation.Builder.class)
public class LocalRepositoryLocation extends RepositoryLocation<LocalRepository> {
    private final File repositoryLocation;

    public LocalRepositoryLocation(Builder builder) {
        super("local");

        this.repositoryLocation = builder.repositoryLocation;
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

        return new EqualsBuilder().append(repositoryLocation, that.repositoryLocation).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(repositoryLocation).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(repositoryLocation).toString();
    }

    @Override
    public String toDisplayString() {
        return String.format("file:%s", repositoryLocation.toString());
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private File repositoryLocation;

        public Builder() {
            // Default constructor
        }

        public Builder(LocalRepositoryLocation location) {
            this.withRepositoryLocation(location.getRepositoryLocation());
        }

        public Builder withRepositoryLocation(File location) {
            this.repositoryLocation = location;
            return this;
        }

        public LocalRepositoryLocation build() {
            return new LocalRepositoryLocation(this);
        }
    }
}
