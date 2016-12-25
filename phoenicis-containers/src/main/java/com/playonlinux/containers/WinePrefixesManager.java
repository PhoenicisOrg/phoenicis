package com.playonlinux.containers;

import com.phoenicis.entities.Architecture;
import com.playonlinux.containers.dto.ContainerDTO;
import com.playonlinux.containers.dto.WinePrefixDTO;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class WinePrefixesManager implements ContainersManager {
    @Value("${application.user.wineprefix}")
    private String winePrefixPath;

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
                containers.add(
                        new WinePrefixDTO(
                                winePrefix.getName(),
                                ContainerDTO.ContainerType.WINEPREFIX,
                                detectWinePrefixArchitecture(winePrefix)
                        ));
            }
            callback.accept(containers);
        }
    }

    private Architecture detectWinePrefixArchitecture(File winePrefix) {
        return Architecture.I386; // FIXME
    }
}
