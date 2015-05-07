package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.domain.Shortcut;
import com.playonlinux.domain.VirtualDrive;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.VirtualDrives;
import com.playonlinux.ui.dtos.VirtualDriveDTO;
import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class VirtualDrivesPlayOnLinuxImplementation extends Observable implements VirtualDrives, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    @Inject
    static PlayOnLinuxBackgroundServicesManager playOnLinuxBackgroundServicesManager;

    private Iterator<VirtualDriveDTO> virtualdrivesDTOInterator;
    private ObservableDirectory observableWineprefixes;


    public VirtualDrivesPlayOnLinuxImplementation() throws PlayOnLinuxError {
        File winePrefixes = playOnLinuxContext.makePrefixesPath();
        observableWineprefixes = new ObservableDirectory(winePrefixes);

        observableWineprefixes.addObserver(this);
        playOnLinuxBackgroundServicesManager.register(observableWineprefixes);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.setChanged();
        this.notifyObservers();
        virtualdrivesDTOInterator = new Iterator<VirtualDriveDTO>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return (observableWineprefixes.getObservedDirectory().listFiles().length > i);
            }

            @Override
            public VirtualDriveDTO next() {
                VirtualDrive virtualDrive =
                        new VirtualDrive(observableWineprefixes.getObservedDirectory().listFiles()[i]);
                i++;
                return new VirtualDriveDTO.Builder()
                        .withName(virtualDrive.getName())
                        .build();
            }
        };
    }

    @Override
    public Iterator<VirtualDriveDTO> iterator() {
        return null;
    }
}
