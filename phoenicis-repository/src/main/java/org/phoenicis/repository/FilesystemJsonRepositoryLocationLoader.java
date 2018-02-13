package org.phoenicis.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.phoenicis.repository.location.ClasspathRepositoryLocation;
import org.phoenicis.repository.location.GitRepositoryLocation;
import org.phoenicis.repository.location.RepositoryLocation;
import org.phoenicis.repository.repositoryTypes.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FilesystemJsonRepositoryLocationLoader implements RepositoryLocationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemJsonRepositoryLocationLoader.class);

    private final String repositoryListPath;
    private final ObjectMapper objectMapper;

    public FilesystemJsonRepositoryLocationLoader(String repositoryListPath, ObjectMapper objectMapper) {
        this.repositoryListPath = repositoryListPath;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<RepositoryLocation<? extends Repository>> loadRepositoryLocations() {
        List<RepositoryLocation<? extends Repository>> result = new ArrayList<>();

        File repositoryListFile = new File(repositoryListPath);

        if (repositoryListFile.exists()) {
            try {
                result = this.objectMapper.readValue(new File(repositoryListPath),
                        TypeFactory.defaultInstance().constructParametricType(List.class, RepositoryLocation.class));
            } catch (IOException e) {
                LOGGER.error("Couldn't load repository location list", e);
            }
        } else {
            try {
                result.add(new GitRepositoryLocation.Builder()
                        .withGitRepositoryUri(new URL("https://github.com/PlayOnLinux/Scripts").toURI()) //FIXME Change to use PhoenicisOrg instead if PlayOnLinux ?
                        .withBranch("master").build());
                result.add(new ClasspathRepositoryLocation("/org/phoenicis/repository"));
            } catch (URISyntaxException | MalformedURLException e) {
                LOGGER.error("Couldn't create default repository location list", e);
            }
        }

        return result;
    }

    @Override
    public void saveRepositories(List<RepositoryLocation<? extends Repository>> repositoryLocations) {
        try {
            objectMapper.writeValue(new File(repositoryListPath), repositoryLocations);
        } catch (IOException e) {
            LOGGER.error("Couldn't save repository location list", e);
        }
    }

}
