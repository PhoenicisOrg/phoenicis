package com.playonlinux.tools;

import com.playonlinux.tools.archive.Extractor;
import com.playonlinux.tools.archive.Tar;
import com.playonlinux.tools.checksum.ChecksumCalculator;
import com.playonlinux.tools.files.FileAnalyser;
import com.playonlinux.tools.files.FileUtilities;
import com.playonlinux.tools.http.Downloader;
import com.playonlinux.tools.system.ArchitectureFetcher;
import com.playonlinux.tools.system.OperatingSystemFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfiguration {
    @Bean
    public FileAnalyser fileAnalyser() {
        return new FileAnalyser();
    }

    @Bean
    public FileUtilities fileUtilities() {
        return new FileUtilities();
    }

    @Bean
    public Tar tar() {
        return new Tar(fileUtilities());
    }

    @Bean
    public Extractor extractor() {
        return new Extractor(fileAnalyser(), tar());
    }

    @Bean
    public Downloader downloader() {
        return new Downloader();
    }

    @Bean
    public ChecksumCalculator checksumCalculator() {
        return new ChecksumCalculator();
    }

    @Bean
    public OperatingSystemFetcher operatingSystemFetcher() {
        return new OperatingSystemFetcher();
    }

    @Bean
    public ArchitectureFetcher architectureFetcher() {
        return new ArchitectureFetcher(operatingSystemFetcher());
    }
}
