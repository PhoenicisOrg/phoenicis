/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.phoenicis.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phoenicis.tools.archive.Extractor;
import com.phoenicis.tools.archive.Tar;
import com.phoenicis.tools.archive.Zip;
import com.phoenicis.tools.checksum.ChecksumCalculator;
import com.phoenicis.tools.config.CompatibleConfigFileFormatFactory;
import com.phoenicis.tools.files.*;
import com.phoenicis.tools.http.Downloader;
import com.phoenicis.tools.system.ArchitectureFetcher;
import com.phoenicis.tools.system.OperatingSystemFetcher;
import com.phoenicis.tools.system.SystemConfiguration;
import com.phoenicis.tools.system.terminal.TerminalOpener;
import com.phoenicis.tools.win32.ExeAnalyser;
import com.phoenicis.win32.Win32Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SystemConfiguration.class)
public class ToolsConfiguration {
    @Autowired
    private Win32Configuration win32Configuration;

    @Autowired
    private SystemConfiguration systemConfiguration;

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
    public FileSizeUtilities fileSizeUtilities() {
        return new FileSizeUtilities();
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
        return new Downloader(fileSizeUtilities());
    }

    @Bean
    public ChecksumCalculator checksumCalculator() {
        return new ChecksumCalculator();
    }

    @Bean
    public OperatingSystemFetcher operatingSystemFetcher() {
        return systemConfiguration.operatingSystemFetcher();
    }

    @Bean
    public ArchitectureFetcher architectureFetcher() {
        return new ArchitectureFetcher(operatingSystemFetcher());
    }

    @Bean
    public ExeAnalyser exeAnalyser() {
        return new ExeAnalyser(win32Configuration.peReader());
    }

    @Bean
    public TerminalOpener terminalOpener() {
        return systemConfiguration.terminalOpener();
    }
}
