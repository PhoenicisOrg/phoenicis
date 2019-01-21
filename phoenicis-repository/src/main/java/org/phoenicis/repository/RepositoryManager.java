package org.phoenicis.repository;

import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.repository.types.Repository;

import java.util.List;
import java.util.function.Consumer;

/**
 * This Interface contains all methods a RepositoryManager must implement.
 *
 * @author marc
 * @since 07.04.17
 */
public interface RepositoryManager {
    /**
     * This method adds a corresponding pair of callbacks to this repository manager
     *
     * @param onRepositoryChange The callback that should be called with the new RepositoryDTO when the repository
     *            change succeeded
     * @param onError The callback that should be called when the repository change failed
     */
    void addCallbacks(Consumer<RepositoryDTO> onRepositoryChange, Consumer<Exception> onError);

    /**
     * This method returns the {@link org.phoenicis.repository.dto.ApplicationDTO}, which can be found at the given
     * path.
     *
     * @param path The path, where the searched ApplicationDTO can be found
     * @return The found ApplicationDTO
     */
    ApplicationDTO getApplication(List<String> path);

    /**
     * This method returns the {@link org.phoenicis.repository.dto.ScriptDTO}, which can be found at the given path
     *
     * @param path The path, where the searched ScriptDTO can be found
     * @return The found ScriptDTO
     */
    ScriptDTO getScript(List<String> path);

    /**
     * This method moves the repository, belonging to the given repository url, to the given index.
     * This is done by swapping the current content at the given index with old index of the given repository url
     * After this method has been called {@link #triggerRepositoryChange()} will be called once.
     *
     * @param repositoryUrl The repository url belonging to the repository that should be moved to @param toIndex
     * @param toIndex The index, to which the repository should be moved
     */
    void moveRepository(RepositoryLocation<? extends Repository> repositoryUrl, int toIndex);

    /**
     * This method adds a number of given repositories to this manager. This is done by inserting the repositories at
     * the given position.
     * After this method has been called {@link #triggerRepositoryChange()} will be called once.
     *
     * @param index The start position, where the repositories should be added
     * @param repositoryUrls An array containing the urls to the to be added repositories
     */
    void addRepositories(int index, RepositoryLocation<? extends Repository>... repositoryUrls);

    /**
     * This method adds a number of given repositories to this manager. This is done by appending the repositories at
     * the end, which makes them the lowest priority.
     * After this method has been called {@link #triggerRepositoryChange()} will be called once.
     *
     * @param repositoryUrls An array containing the urls to the to be added repositories
     */
    void addRepositories(RepositoryLocation<? extends Repository>... repositoryUrls);

    /**
     * This method removes the repositories belonging to the given array of repository urls from this manager.
     * After this method has been called {@link #triggerRepositoryChange()} will be called once.
     *
     * @param repositoryUrls An array containing the urls of the to be removed repositories.
     */
    void removeRepositories(RepositoryLocation<? extends Repository>... repositoryUrls);

    /**
     * This method will fetch a new list of {@link org.phoenicis.repository.dto.CategoryDTO}s from the managed
     * repositories.
     * After the new category dtos have been fetched, this method will call the previously added onRepositoryChange
     * callbacks with the newly fetched category dtos.
     * If an error appeared, the onError callbacks will be called, with the error.
     */
    void triggerRepositoryChange();

    /**
     * This method triggers all registered callbacks.
     * In contrast to {@link #triggerRepositoryChange()}, it does not update the repository before.
     */
    void triggerCallbacks();
}
