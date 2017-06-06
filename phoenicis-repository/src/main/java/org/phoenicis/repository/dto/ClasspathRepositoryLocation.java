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
@JsonDeserialize(builder = ClasspathRepositoryLocation.Builder.class)
public class ClasspathRepositoryLocation extends RepositoryLocation<ClasspathRepository> {
    private final String packagePath;

    public ClasspathRepositoryLocation(Builder builder) {
        super("classpath");

        this.packagePath = builder.packagePath;
    }

    @Override
    public ClasspathRepository createRepository(String cacheDirectoryPath,
            LocalRepository.Factory localRepositoryFactory, ClasspathRepository.Factory classPathRepositoryFactory,
            FileUtilities fileUtilities) {
        return classPathRepositoryFactory.createInstance(packagePath);
    }

    public String getPackagePath() {
        return packagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClasspathRepositoryLocation that = (ClasspathRepositoryLocation) o;

        return new EqualsBuilder().append(packagePath, that.packagePath).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(packagePath).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(packagePath).toString();
    }

    @Override
    public String toDisplayString() {
        return String.format("classpath://%s", packagePath);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private String packagePath;

        public Builder() {
            // Default constructor
        }

        public Builder(ClasspathRepositoryLocation location) {
            this.withPackagePath(location.getPackagePath());
        }

        public Builder withPackagePath(String packagePath) {
            this.packagePath = packagePath;
            return this;
        }

        public ClasspathRepositoryLocation build() {
            return new ClasspathRepositoryLocation(this);
        }
    }
}
