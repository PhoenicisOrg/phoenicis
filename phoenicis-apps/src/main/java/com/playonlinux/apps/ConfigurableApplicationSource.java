package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ConfigurableApplicationSource implements ApplicationsSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableApplicationSource.class);

    private final LocalApplicationsSource.Factory localApplicationsSourceFactory;
    private final ApplicationsSource applicationsSource;

    ConfigurableApplicationSource(String sourceUrl, LocalApplicationsSource.Factory localApplicationsSourceFactory) {
        this.localApplicationsSourceFactory = localApplicationsSourceFactory;
        final String[] urls = sourceUrl.split(";");
        applicationsSource = new MultipleApplicationSource(Arrays.stream(urls).map(this::toApplicationSource).collect(Collectors.toList()));
    }

    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return applicationsSource.fetchInstallableApplications();
    }

    private ApplicationsSource toApplicationSource(String applicationSourceUrl) {
        LOGGER.info("Registering: " + applicationSourceUrl);
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
