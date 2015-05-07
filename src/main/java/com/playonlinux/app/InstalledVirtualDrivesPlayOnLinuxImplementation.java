package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.domain.VirtualDrive;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.InstalledVirtualDrives;
import com.playonlinux.ui.dtos.VirtualDriveDTO;
import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class InstalledVirtualDrivesPlayOnLinuxImplementation extends Observable implements InstalledVirtualDrives, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private Iterator<VirtualDriveDTO> virtualdrivesDTOInterator;



    public InstalledVirtualDrivesPlayOnLinuxImplementation() throws PlayOnLinuxError {
        File winePrefixes = playOnLinuxContext.makePrefixesPath();
        final ObservableDirectory observableWineprefixes = new ObservableDirectory(winePrefixes);

        observableWineprefixes.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(observableWineprefixes);
    }

    @Override
    public void update(Observable o, Object directoryContent) {
        this.setChanged();
        this.notifyObservers();
        File[] directoryContentCasted = (File[]) directoryContent;
        virtualdrivesDTOInterator = new Iterator<VirtualDriveDTO>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return (directoryContentCasted.length > i);
            }

            @Override
            public VirtualDriveDTO next() {
                VirtualDrive virtualDrive =
                        new VirtualDrive(directoryContentCasted[i]);
                i++;
                return new VirtualDriveDTO.Builder()
                        .withName(virtualDrive.getName())
                        .build();
            }
        };
    }

    @Override
    public Iterator<VirtualDriveDTO> iterator() {
        return virtualdrivesDTOInterator;
    }
}
