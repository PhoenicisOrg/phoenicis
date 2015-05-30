/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.webservice;

import com.playonlinux.common.api.services.BackgroundService;
import com.playonlinux.common.Progressable;
import com.playonlinux.common.dto.ProgressStateDTO;
import org.apache.log4j.Logger;

/**
 * This class download a given script from PlayOnLinux webservices
 */
public class RemoteInstallerDownloader extends Progressable {

    private final String url;
    private String script;
    private Runnable callBack;
    private final static Logger logger = Logger.getLogger(RemoteInstallerDownloader.class);
    public RemoteInstallerDownloader() {
        this.url = null;
    }

    public BackgroundService createTask(String selectedItemLabel) {
        return new Task(this);
    }

    public RemoteInstallerDownloader withScript(String scriptName) {
        this.script = scriptName;
        return this;
    }

    public RemoteInstallerDownloader withCallBack(Runnable callBack) {
        this.callBack = callBack;
        return this;
    }

    public void runCallback() {
        if(callBack != null) {
            this.callBack.run();
        }
    }

    private class Task implements BackgroundService {
        DownloadThread downloadThread;
        RemoteInstallerDownloader remoteInstallerDownloader;

        Task(RemoteInstallerDownloader remoteInstallerDownloader) {
            this.remoteInstallerDownloader = remoteInstallerDownloader;
            downloadThread = new DownloadThread(remoteInstallerDownloader);
        }
        @Override
        public void shutdown() {
            downloadThread.shutdown();
        }

        @Override
        public void start() {
            downloadThread.start();
        }
    }

    private class DownloadThread extends Thread {
        private final RemoteInstallerDownloader remoteInstallerDownloader;

        public DownloadThread(RemoteInstallerDownloader remoteInstallerDownloader) {
            super();
            this.remoteInstallerDownloader = remoteInstallerDownloader;
        }

        public void run() {
            remoteInstallerDownloader.setState(ProgressStateDTO.State.PROGRESSING);
            for(float i = 0; i < 100; i += 0.1) {
                remoteInstallerDownloader.setPercentage(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.info("Download was interrupted", e);
                }
            }
            remoteInstallerDownloader.setState(ProgressStateDTO.State.SUCCESS);
            remoteInstallerDownloader.runCallback();
        }


        public void shutdown() {
            this.interrupt();
        }
    }
}
