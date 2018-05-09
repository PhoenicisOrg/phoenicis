package org.phoenicis.repository.location;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.phoenicis.repository.types.ClasspathRepository;
import org.phoenicis.repository.types.LocalRepository;
import org.phoenicis.repository.types.Repository;
import org.phoenicis.tools.files.FileUtilities;

/**
 * Required basic functionality and information for a repository location
 *
 * @author marc
 * @since 06.06.17
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = LocalRepositoryLocation.class, name = "local"),
        @JsonSubTypes.Type(value = ClasspathRepositoryLocation.class, name = "classpath"),
        @JsonSubTypes.Type(value = GitRepositoryLocation.class, name = "git") })
public abstract class RepositoryLocation<E extends Repository> {
    /**
     * The repository location type. Currently the following are available:
     * <ul>
     * <li>local</li>
     * <li>classpath</li>
     * <li>git</li>
     * </ul>
     */
    private final String type;

    /**
     * Constructor
     *
     * @param type The used repository location type
     */
    protected RepositoryLocation(String type) {
        this.type = type;
    }

    /**
     * Creates a new repository of type <code>E</code> and returns it.
     *
     * @param cacheDirectoryPath The directory where remote repositories are locally cached
     * @param localRepositoryFactory Factory for {@link LocalRepository} instances
     * @param classPathRepositoryFactory Factory for {@link ClasspathRepository} instances
     * @param fileUtilities FileUtilities object
     * @return The newly created repository
     */
    public abstract E createRepository(String cacheDirectoryPath, LocalRepository.Factory localRepositoryFactory,
            ClasspathRepository.Factory classPathRepositoryFactory, FileUtilities fileUtilities);

    /**
     * Returns a String to be displayed to the user, containing all available information about the location of the
     * repository
     *
     * @return The to be displayed String
     */
    public abstract String toDisplayString();

    /**
     * The repository location type
     *
     * @return The repository location type
     */
    public String getType() {
        return type;
    }
}
