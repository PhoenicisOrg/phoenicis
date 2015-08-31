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

public class NullSetupWizard implements SetupWizard {
    @Override
    public void init() {

    }

    @Override
    public void setLeftImage(String leftImage) throws IOException {

    }

    @Override
    public void setTopImage(String topImage) throws IOException {

    }

    @Override
    public void message(String textToShow) throws CancelException {

    }

    @Override
    public void presentation(String programName, String programEditor, String editorURL, String scriptorName, String prefixName) throws CancelException {

    }

    @Override
    public void presentation(String textToShow) throws CancelException {

    }

    @Override
    public void licenceFile(String textToShow, File licenceFile) throws CancelException {

    }

    @Override
    public void licenceFile(String textToShow, String licenceFilePath) throws CancelException {

    }

    @Override
    public String licence(String textToShow, String licenceText) throws CancelException {
        return null;
    }

    @Override
    public String textbox(String textToShow) throws CancelException {
        return null;
    }

    @Override
    public String textbox(String textToShow, String defaultValue) throws CancelException {
        return null;
    }

    @Override
    public String menu(String textToShow, List<String> menuItems) throws CancelException {
        return null;
    }

    @Override
    public String browse(String textToShow) throws CancelException {
        return null;
    }

    @Override
    public String browse(String textToShow, String directory, List<String> allowedExtensions) throws CancelException {
        return null;
    }

    @Override
    public void wait(String textToShow) {

    }

    @Override
    public ProgressControl progressBar(String textToShow) throws CancelException {
        return null;
    }

    @Override
    public ScriptLogger getLogContext() throws ScriptFailureException {
        return null;
    }

    @Override
    public void log(String message) throws ScriptFailureException {

    }

    @Override
    public void log(String message, Throwable e) throws ScriptFailureException {

    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void registerComponent(SetupWizardComponent setupWizardComponent) {

    }

    @Override
    public void close() {

    }
}
