package com.playonlinux.app;

import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.domain.Shortcut;
import com.playonlinux.domain.ShortcutSet;
import com.playonlinux.injection.Component;
import com.playonlinux.injection.Inject;
import com.playonlinux.ui.api.InstalledApplications;
import com.playonlinux.ui.beans.ShortcutBean;
import com.playonlinux.utils.ObservableDirectory;

import java.io.File;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

@Component
public class PlayOnLinuxInstalledApplicationImplementation extends Observable implements InstalledApplications, Observer {
    @Inject
    static PlayOnLinuxContext playOnLinuxContext;

    ShortcutSet shortcutSet;
    private Iterator<ShortcutBean> shortcutBeanIterator;

    PlayOnLinuxInstalledApplicationImplementation() throws PlayOnLinuxError {
        File shortcutDirectory = playOnLinuxContext.makeShortcutsScriptsPath();
        File iconDirectory = playOnLinuxContext.makeShortcutsIconsPath();
        File configFilesDirectory = playOnLinuxContext.makeShortcutsConfigPath();

        ObservableDirectory shortcutDirectoryObservable = new ObservableDirectory(shortcutDirectory);
        ObservableDirectory iconDirectoryObservable = new ObservableDirectory(iconDirectory);

        shortcutDirectoryObservable.addObserver(this);
        iconDirectoryObservable.addObserver(this);

        shortcutDirectoryObservable.start();
        iconDirectoryObservable.start();

        shortcutSet = new ShortcutSet(shortcutDirectoryObservable, iconDirectoryObservable,
                configFilesDirectory);

        shortcutSet.addObserver(this);
    }


    @Override
    public synchronized void update(Observable o, Object arg) {
        this.setChanged();
        this.notifyObservers();
        shortcutBeanIterator = new Iterator<ShortcutBean>() {
            volatile int i = 0;

            @Override
            public boolean hasNext() {
                return (shortcutSet.getShortcuts().size() > i);
            }

            @Override
            public ShortcutBean next() {
                Shortcut shortcut = shortcutSet.getShortcuts().get(i);
                i++;
                return new ShortcutBean.Builder()
                        .withName(shortcut.getShortcutName())
                        .withIcon(shortcut.getIconPath())
                        .build();
            }
        };
    }

    @Override
    synchronized public Iterator<ShortcutBean> iterator() {
        return this.shortcutBeanIterator;
    }

}
