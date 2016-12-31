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

package com.playonlinux.scripts.wizard;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CompleteWizard extends Wizard, ProgressWizard, ChoiceWizard, LogWizard, WineWizard {
    void setLeftImage(String leftImage) throws IOException;

    void setTopImage(String topImage) throws IOException;

    Void message(String textToShow);

    Void presentation(String programName, String programEditor, String applicationHomepage, String scriptorName);

    Void presentation(String textToShow);

    Void licenceFile(String textToShow, File licenceFile);

    Void licenceFile(String textToShow, String licenceFilePath);

    Void licence(String textToShow, String licenceText);

    String textbox(String textToShow);

    String textbox(String textToShow, String defaultValue);

    String browse(String textToShow);

    String browse(String textToShow, String directory, List<String> allowedExtensions);

    Void wait(String textToShow);
}
