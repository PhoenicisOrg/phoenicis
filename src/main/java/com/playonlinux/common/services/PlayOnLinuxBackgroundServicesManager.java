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

package com.playonlinux.common.services;

import com.playonlinux.common.api.services.BackgroundService;

import java.util.ArrayList;
import java.util.List;

public class PlayOnLinuxBackgroundServicesManager {
    List<BackgroundService> runningTask;

    public PlayOnLinuxBackgroundServicesManager() {
        runningTask = new ArrayList<>();
    }

    public void register(BackgroundService backgroundService) {
        runningTask.add(backgroundService);
        backgroundService.start();
    }

    public void shutdown() {
        runningTask.forEach(BackgroundService::shutdown);
    }

    public void unregister(BackgroundService backgroundService) {
        runningTask.remove(backgroundService);
        backgroundService.shutdown();
    }
}
