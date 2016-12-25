package com.playonlinux.containers;

import com.phoenicis.entities.Architecture;
import com.playonlinux.containers.dto.ContainerDTO;
import com.playonlinux.containers.dto.WinePrefixDTO;
import com.playonlinux.tools.config.CompatibleConfigFileFormatFactory;
import com.playonlinux.tools.config.ConfigFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WinePrefixesManager implements ContainersManager {
    @Value("${application.user.wineprefix}")
    private String winePrefixPath;

    private final CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory;

    public WinePrefixesManager(CompatibleConfigFileFormatFactory compatibleConfigFileFormatFactory) {
        this.compatibleConfigFileFormatFactory = compatibleConfigFileFormatFactory;
    }

    @Override
    public void fetchContainers(Consumer<List<ContainerDTO>> callback, Consumer<Exception> errorCallback) {
        final File winePrefixesFile = new File(winePrefixPath);
        winePrefixesFile.mkdirs();

        final File[] winePrefixes = winePrefixesFile.listFiles();


        if(winePrefixes == null) {
            callback.accept(Collections.emptyList());
        } else {
            final List<ContainerDTO> containers = new ArrayList<>();
            for (File winePrefix : winePrefixes) {
                final ConfigFile configFile = compatibleConfigFileFormatFactory.open(new File(winePrefix, "playonlinux.cfg"));

                containers.add(
                        new WinePrefixDTO(
                                winePrefix.getName(),
                                winePrefix.getAbsolutePath(),
                                ContainerDTO.ContainerType.WINEPREFIX,
                                configFile.readValue("wineArchitecture", ""),
                                configFile.readValue("wineDistribution", ""),
                                configFile.readValue("wineVersion", "")
                        ));
            }
            callback.accept(containers);
        }
    }

}
