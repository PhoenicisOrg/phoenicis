package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurableApplicationSource implements ApplicationsSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableApplicationSource.class);

    private final String sourceUrl;
    private final LocalApplicationsSource.Factory localApplicationsSourceFactory;

    public ConfigurableApplicationSource(String sourceUrl, LocalApplicationsSource.Factory localApplicationsSourceFactory) {
        this.sourceUrl = sourceUrl;
        this.localApplicationsSourceFactory = localApplicationsSourceFactory;
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        final String[] urls = sourceUrl.split(";");
        final ApplicationsSource applicationsSource =
                new MultipleApplicationSource(Arrays.stream(urls).map(this::toApplicationSource).collect(Collectors.toList()));
        return applicationsSource.fetchInstallableApplications();
    }

    private ApplicationsSource toApplicationSource(String applicationSourceUrl) {
        try {
            final URI url = new URI(applicationSourceUrl);
            final String scheme = url.getScheme().split("\\+")[0];

            switch (scheme) {
                case "git":
                    return new GitApplicationsSource(applicationSourceUrl.replace("git+",""), localApplicationsSourceFactory);
                case "file":
                    return localApplicationsSourceFactory.createInstance(url.getRawPath());
                default:
                    LOGGER.warn("Unsupported URL: " + applicationSourceUrl);
                    return new NullApplicationSource();
            }
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot parse URL: " + applicationSourceUrl, e);
            return new NullApplicationSource();
        }
    }
}
