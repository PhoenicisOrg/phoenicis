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

package com.playonlinux.apps.entities;

/**
 * Entity that represents the state of a script that is being downloaded
 */
public class InstallerDownloaderEntity {
    private final boolean finished;
    private final boolean failed;
    private final boolean signatureError;
    private final double percentage;
    private final String scriptContent;

    public InstallerDownloaderEntity(boolean finished, boolean failed, boolean signatureError, double percentage,
            String scriptContent) {
        this.finished = finished;
        this.failed = failed;
        this.signatureError = signatureError;
        this.percentage = percentage;
        this.scriptContent = scriptContent;
    }

    public boolean isFailed() {
        return failed;
    }

    public boolean isFinished() {
        return finished;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public boolean isSignatureError() {
        return signatureError;
    }

}
