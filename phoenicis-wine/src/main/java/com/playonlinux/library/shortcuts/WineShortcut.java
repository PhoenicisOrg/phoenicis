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

package com.playonlinux.library.shortcuts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.framework.wizard.WineWizard;
import com.playonlinux.library.runners.WineShortcutRunner;

import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonDeserialize(builder = WineShortcut.Builder.class)
public class WineShortcut implements Shortcut {
    private final String wineDebug;
    private final String winePrefix;
    private final String workingDirectory;
    private final String executableName;
    private final List<String> arguments;

    public WineShortcut(Builder builder) {
        this.wineDebug = builder.wineDebug;
        this.winePrefix = builder.winePrefix;
        this.workingDirectory = builder.workingDirectory;
        this.executableName = builder.executableName;
        this.arguments = builder.arguments;
    }
    
    @Override
    public void execute(WineWizard wineWizard) throws CancelException {
        new WineShortcutRunner(this).run(wineWizard);
    }

    @JsonPOJOBuilder(buildMethodName = "create", withPrefix = "with")
    public static class Builder {
        private String wineDebug = "-all";
        private String winePrefix;
        private String workingDirectory;
        private String executableName;
        private List<String> arguments;

        public String getWorkingDirectory() {
            return workingDirectory;
        }

        public String getWinePrefix() {
            return winePrefix;
        }

        public String getExecutableName() {
            return executableName;
        }

        public Builder withWineDebug(String wineDebug) {
            this.wineDebug = wineDebug;
            return this;
        }

        public Builder withArguments(List<String> arguments) {
            this.arguments = arguments;
            return this;
        }

        public Builder withExecutableName(String executableName) {
            this.executableName = executableName;
            return this;
        }

        public Builder withWorkingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        public Builder withWinePrefix(String winePrefix) {
            this.winePrefix = winePrefix;
            return this;
        }

        public WineShortcut create() {
            return new WineShortcut(this);
        }
    }
}
