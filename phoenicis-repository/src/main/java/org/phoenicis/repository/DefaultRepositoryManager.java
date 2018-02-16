package org.phoenicis.repository;

import org.phoenicis.configuration.security.Safe;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.*;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static org.phoenicis.configuration.localisation.Localisation.tr;

/**
 * Created by marc on 31.03.17.
 */
@Safe
public class DefaultRepositoryManager implements RepositoryManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRepositoryManager.class);

    private final LocalRepository.Factory localRepositoryFactory;
    private final ClasspathRepository.Factory classPathRepositoryFactory;
    private final String cacheDirectoryPath;

    private final FileUtilities fileUtilities;

    private Map<RepositoryLocation<? extends Repository>, Repository> repositoryMap;

    private MultipleRepository multipleRepository;
    private CachedRepository cachedRepository;
    private BackgroundRepository backgroundRepository;

    private List<CallbackPair> callbacks;

    public DefaultRepositoryManager(ExecutorService executorService, boolean enforceUncompatibleOperatingSystems,
            ToolsConfiguration toolsConfiguration, String cacheDirectoryPath, FileUtilities fileUtilities,
            LocalRepository.Factory localRepositoryFactory, ClasspathRepository.Factory classPathRepositoryFactory,
            BackgroundRepository.Factory backgroundRepositoryFactory) {
        super();

        this.localRepositoryFactory = localRepositoryFactory;
        this.classPathRepositoryFactory = classPathRepositoryFactory;
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.fileUtilities = fileUtilities;

        this.repositoryMap = new HashMap<>();
        this.callbacks = new ArrayList<>();

        this.multipleRepository = new MultipleRepository();
        this.cachedRepository = new CachedRepository(multipleRepository);
        this.backgroundRepository = backgroundRepositoryFactory.createInstance(cachedRepository, executorService);
    }

    @Override
    public void addCallbacks(Consumer<RepositoryDTO> onRepositoryChange, Consumer<Exception> onError) {
        this.callbacks.add(new CallbackPair(onRepositoryChange, onError));
    }

    @Override
    public ApplicationDTO getApplication(List<String> path) {
        return this.cachedRepository.getApplication(path);
    }

    @Override
    public ScriptDTO getScript(List<String> path) {
        return this.cachedRepository.getScript(path);
    }

    @Override
    public void moveRepository(RepositoryLocation<? extends Repository> repositoryUrl, int toIndex) {
        LOGGER.info(String.format("Move repository: %s to %d", repositoryUrl, toIndex));

        this.multipleRepository.moveRepository(this.repositoryMap.get(repositoryUrl), toIndex);

        this.triggerRepositoryChange();
    }

    @Override
    public void addRepositories(int index, RepositoryLocation<? extends Repository>... repositoryUrls) {
        LOGGER.info(String.format("Adding repositories: %s at index %d", Arrays.toString(repositoryUrls), index));

  for (int repositoryUrlIndex = 0; repositoryUrlIndex < repositoryUrls.length; repositoryUrlIndex++) {
            Repository repository = repositoryUrls[repositoryUrlIndex].createRepository(cacheDirectoryPath,
                    localRepositoryFactory, classPathRepositoryFactory, fileUtilities);

            this.repositoryMap.put(repositoryUrls[repositoryUrlIndex], repository);

            this.multipleRepository.addRepository(index + repositoryUrlIndex, repository);
        }

        this.triggerRepositoryChange();
    }

    @Override
    public void addRepositories(RepositoryLocation<? extends Repository>... repositoryUrls) {
        this.addRepositories(this.multipleRepository.size(), repositoryUrls);
    }

    @Override
    public void removeRepositories(RepositoryLocation<? extends Repository>... repositoryUrls) {
        LOGGER.info(String.format("Removing repositories: %s", Arrays.toString(repositoryUrls)));

        for (RepositoryLocation<? extends Repository> repositoryLocation : repositoryUrls) {
            Repository deletedRepository = this.repositoryMap.remove(repositoryLocation);

            // remove repository from repository collection
            this.multipleRepository.removeRepository(deletedRepository);

            // do eventual cleanup work
            deletedRepository.onDelete();
        }

        this.triggerRepositoryChange();
    }

    @Override
    public void triggerRepositoryChange() {
        this.cachedRepository.clearCache();

        if (!this.callbacks.isEmpty()) {
            this.backgroundRepository.fetchInstallableApplications(repositoryDTO -> {
                this.callbacks.forEach(callbackPair -> callbackPair.getOnRepositoryChange().accept(tr(repositoryDTO)));
            }, exception -> this.callbacks.forEach(callbackPair -> callbackPair.getOnError().accept(exception)));
        }
    }

    private class CallbackPair {
        private Consumer<RepositoryDTO> onRepositoryChange;

        private Consumer<Exception> onError;

        public CallbackPair(Consumer<RepositoryDTO> onRepositoryChange, Consumer<Exception> onError) {
            this.onRepositoryChange = onRepositoryChange;
            this.onError = onError;
        }

        public Consumer<RepositoryDTO> getOnRepositoryChange() {
            return this.onRepositoryChange;
        }

        public Consumer<Exception> getOnError() {
            return this.onError;
        }
    }
}
