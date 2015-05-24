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

/**
 * This class download a given script from PlayOnLinux webservices
 */
public class RemoteInstallerDownloader extends Progressable {

    private final String url;

    public RemoteInstallerDownloader() {
        this.url = null;
    }

    String fetchScript(String scriptName) {
        return null;
    }

    public BackgroundService createTask(String selectedItemLabel) {
        return new Task();
    }


    private class Task implements BackgroundService {
        DownloadThread downloadThread;
        Task() {
            downloadThread = new DownloadThread();
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
        public void run() {
            setState(Progressable.State.RUNNING);
            for(float i = 0; i < 100; i += 0.1) {
                setPercentage(i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        public void shutdown() {

        }
    }
}
