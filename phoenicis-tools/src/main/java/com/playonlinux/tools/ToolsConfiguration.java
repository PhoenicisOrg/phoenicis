package com.playonlinux.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playonlinux.tools.archive.Extractor;
import com.playonlinux.tools.archive.Tar;
import com.playonlinux.tools.archive.Zip;
import com.playonlinux.tools.checksum.ChecksumCalculator;
import com.playonlinux.tools.config.CompatibleConfigFileFormatFactory;
import com.playonlinux.tools.files.*;
import com.playonlinux.tools.http.Downloader;
import com.playonlinux.tools.system.ArchitectureFetcher;
import com.playonlinux.tools.system.OperatingSystemFetcher;
import com.playonlinux.tools.win32.ExeAnalyser;
import com.playonlinux.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolsConfiguration {
    @Autowired
    private Win32Configuration win32Configuration;

    @Bean
    public CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory() {
        return new CompatibleConfigFileFormatFactory(new ObjectMapper());
    }

    @Bean
    public FileAnalyser fileAnalyser() {
        return new FileAnalyser();
    }

    @Bean
    public FileUtilities fileUtilities() {
        return new FileUtilities();
    }

    @Bean
    public FileSearcher fileSearcher() {
        return new FileSearcher();
    }

    @Bean
    Tar tar() {
        return new Tar(fileUtilities());
    }

    @Bean
    Zip zip() {
        return new Zip();
    }

    @Bean
    public Extractor extractor() {
        return new Extractor(fileAnalyser(), tar(), zip());
    }

    @Bean
    public FileCopier fileCopier() {
        return new FileCopier();
    }

    @Bean
    public FreeSpaceFetcher freeSpaceFetcher() {
        return new FreeSpaceFetcher();
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

    @Bean
    public ExeAnalyser exeAnalyser() {
        return new ExeAnalyser(win32Configuration.peReader());
    }
}
