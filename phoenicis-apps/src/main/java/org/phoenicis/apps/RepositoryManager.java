package org.phoenicis.apps;

import org.phoenicis.apps.dto.CategoryDTO;
import org.phoenicis.multithreading.ControlledThreadPoolExecutorService;
import org.phoenicis.tools.ToolsConfiguration;
import org.phoenicis.tools.files.FileUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by marc on 31.03.17.
 */
public class RepositoryManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryManager.class);

    private final LocalRepository.Factory localRepositoryFactory;
    private final ClasspathRepository.Factory classPathRepositoryFactory;
    private final String cacheDirectoryPath;

    private final FileUtilities fileUtilities;

    private MultipleRepository multipleRepository;
    private FilterRepository filterRepository;
    private CachedRepository cachedRepository;
    private BackgroundRepository backgroundRepository;

    private Consumer<List<CategoryDTO>> onRepositoryChange;
    private Consumer<Exception> onError;

    public RepositoryManager(ExecutorService executorService, boolean enforceUncompatibleOperatingSystems, ToolsConfiguration toolsConfiguration, String cacheDirectoryPath, FileUtilities fileUtilities, LocalRepository.Factory localRepositoryFactory, ClasspathRepository.Factory classPathRepositoryFactory) {
        super();

        this.localRepositoryFactory = localRepositoryFactory;
        this.classPathRepositoryFactory = classPathRepositoryFactory;
        this.cacheDirectoryPath = cacheDirectoryPath;
        this.fileUtilities = fileUtilities;

        this.multipleRepository = new MultipleRepository();
        this.filterRepository = new FilterRepository(multipleRepository, toolsConfiguration.operatingSystemFetcher(), enforceUncompatibleOperatingSystems);
        this.cachedRepository = new CachedRepository(filterRepository);
        this.backgroundRepository = new BackgroundRepository(cachedRepository, executorService);
    }

    public void setOnRepositoryChange(Consumer<List<CategoryDTO>> onRepositoryChange) {
        this.onRepositoryChange = onRepositoryChange;
    }

    public void setOnError(Consumer<Exception> onError) {
        this.onError = onError;
    }

    public Repository cachedRepository() {
        return this.cachedRepository;
    }

    @Deprecated
    public void setFilter(CombinedAppsFilter filter) {
        this.filterRepository.setFilter(filter);
        this.triggerRepositoryChange();
    }

    public void moveRepository(String repositoryUrl, int toIndex) {
        LOGGER.info(String.format("Move repository: %s to %d", repositoryUrl, toIndex));
        this.multipleRepository.moveRepository(toRepository(repositoryUrl), toIndex);
        this.triggerRepositoryChange();
    }

    public void addRepositories(String ... repositoryUrls) {
        LOGGER.info(String.format("Adding repositories: %s", Arrays.toString(repositoryUrls)));
        Arrays.stream(repositoryUrls).map(this::toRepository).forEach(this.multipleRepository::addRepository);
        this.triggerRepositoryChange();
    }

    public void addRepository(String repositoryUrl) {
        LOGGER.info(String.format("Adding repository: %s", repositoryUrl));
        this.multipleRepository.addRepository(toRepository(repositoryUrl));
        this.triggerRepositoryChange();
    }

    public void removeRepositories(String ... repositoryUrls) {
        LOGGER.info(String.format("Removing repositories: %s", Arrays.toString(repositoryUrls)));

        List<Repository> toDeleteRepositories = Arrays.stream(repositoryUrls).map(this::toRepository).collect(Collectors.toList());
        toDeleteRepositories.forEach(this.multipleRepository::removeRepository);

        this.triggerRepositoryChange();

        toDeleteRepositories.forEach(Repository::onDelete);
    }

    public void removeRepository(String repositoryUrl) {
        LOGGER.info(String.format("Removing repository: %s", repositoryUrl));
        Repository toDelete = toRepository(repositoryUrl);
        this.multipleRepository.removeRepository(toDelete);
        this.triggerRepositoryChange();
        toDelete.onDelete();
    }

    public void triggerRepositoryChange() {
        this.cachedRepository.clearCache();

        if (this.onRepositoryChange != null && this.onError != null) {
            this.backgroundRepository.fetchInstallableApplications(onRepositoryChange, onError);
        }
    }

    private Repository toRepository(String repositoryUrl) {
        LOGGER.info("Converting: " + repositoryUrl + " to Repository");
        try {
            final URI url = new URI(repositoryUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitRepository(repositoryUrl.replace("git+",""), cacheDirectoryPath,
                            localRepositoryFactory, fileUtilities);
                case "file":
                    return localRepositoryFactory.createInstance(url.getRawPath());
                case "classpath":
                    return classPathRepositoryFactory.createInstance(url.getPath());
                default:
                    LOGGER.warn("Unsupported URL: " + repositoryUrl);
                    return new NullRepository();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + repositoryUrl, e);
            return new NullRepository();
        }
    }
}
