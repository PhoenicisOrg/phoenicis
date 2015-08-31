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

package com.playonlinux.framework;

import com.playonlinux.core.log.ScriptLogger;
import com.playonlinux.core.scripts.CancelException;
import com.playonlinux.ui.api.ProgressControl;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SetupWizard extends AutoCloseable {
    void init();

    void setLeftImage(String leftImage) throws IOException;

    void setTopImage(String topImage) throws IOException;

    void message(String textToShow) throws CancelException;

    void presentation(String programName, String programEditor, String editorURL, String scriptorName, String prefixName) throws CancelException;

    void presentation(String textToShow) throws CancelException;

    void licenceFile(String textToShow, File licenceFile) throws CancelException;

    void licenceFile(String textToShow, String licenceFilePath) throws CancelException;

    String licence(String textToShow, String licenceText) throws CancelException;

    String textbox(String textToShow) throws CancelException;

    String textbox(String textToShow, String defaultValue) throws CancelException;

    String menu(String textToShow, List<String> menuItems) throws CancelException;

    String browse(String textToShow) throws CancelException;

    String browse(String textToShow, String directory, List<String> allowedExtensions) throws CancelException;

    void wait(String textToShow);

    ProgressControl progressBar(String textToShow) throws CancelException;

    ScriptLogger getLogContext() throws ScriptFailureException;

    void log(String message) throws ScriptFailureException;

    void log(String message, Throwable e) throws ScriptFailureException;

    String getTitle();

    void registerComponent(SetupWizardComponent setupWizardComponent);
}
