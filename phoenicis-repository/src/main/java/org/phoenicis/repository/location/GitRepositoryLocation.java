package org.phoenicis.repository.location;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.phoenicis.repository.repositoryTypes.ClasspathRepository;
import org.phoenicis.repository.repositoryTypes.GitRepository;
import org.phoenicis.repository.repositoryTypes.LocalRepository;
import org.phoenicis.tools.files.FileUtilities;

import java.net.URI;

/**
 * Location information for a repository located inside a git repository
 *
 * @author marc
 * @since 06.06.17
 */
@JsonDeserialize(builder = GitRepositoryLocation.Builder.class)
public class GitRepositoryLocation extends RepositoryLocation<GitRepository> {
    /**
     * The uri belonging to the location of the git repository
     */
    private final URI gitRepositoryUri;

    /**
     * The branch in which the repository is located
     */
    private final String branch;

    /**
     * Constructor
     *
     * @param builder The builder object, containing the values for this {@link GitRepositoryLocation}
     */
    public GitRepositoryLocation(Builder builder) {
        super("git");

        this.gitRepositoryUri = builder.gitRepositoryUri;
        this.branch = builder.branch;
    }

    @Override
    public GitRepository createRepository(String cacheDirectoryPath, LocalRepository.Factory localRepositoryFactory,
            ClasspathRepository.Factory classPathRepositoryFactory, FileUtilities fileUtilities) {
        return new GitRepository(gitRepositoryUri, branch, cacheDirectoryPath, localRepositoryFactory, fileUtilities);
    }

    public URI getGitRepositoryUri() {
        return gitRepositoryUri;
    }

    public String getBranch() {
        return branch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GitRepositoryLocation that = (GitRepositoryLocation) o;

        return new EqualsBuilder().append(gitRepositoryUri, that.gitRepositoryUri).append(branch, that.branch)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gitRepositoryUri).append(branch).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(gitRepositoryUri).append(branch).toString();
    }

    @Override
    public String toDisplayString() {
        return String.format("git+%s:%s", gitRepositoryUri.toString(), branch);
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "with")
    public static class Builder {
        private URI gitRepositoryUri;

        private String branch;

        public Builder() {
            // Default constructor
        }

        public Builder(GitRepositoryLocation location) {
            this.withGitRepositoryUri(location.getGitRepositoryUri());
            this.withBranch(location.getBranch());
        }

        public Builder withGitRepositoryUri(URI gitRepositoryUri) {
            this.gitRepositoryUri = gitRepositoryUri;
            return this;
        }

        public Builder withBranch(String branch) {
            this.branch = branch;
            return this;
        }

        public GitRepositoryLocation build() {
            return new GitRepositoryLocation(this);
        }
    }
}
