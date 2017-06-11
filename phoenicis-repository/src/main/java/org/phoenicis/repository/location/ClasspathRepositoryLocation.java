package org.phoenicis.repository.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.tools.files.FileUtilities;

/**
 * Location information for a repository located inside the classpath of Phoenicis
 *
 * @author marc
 * @since 06.06.17
 */
@JsonDeserialize
public class ClasspathRepositoryLocation extends RepositoryLocation<ClasspathRepository> {
    /**
     * The path to the package containing the classpath repository content
     */
    private final String packagePath;

    /**
     * Constructor
     *
     * @param packagePath The path to the package containing the classpath repository content
     */
    @JsonCreator
    public ClasspathRepositoryLocation(@JsonProperty("packagePath") String packagePath) {
        super("classpath");

        this.packagePath = packagePath;
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
}
