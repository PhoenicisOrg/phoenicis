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

package com.playonlinux.mock;

import static java.lang.String.format;

import com.playonlinux.core.entities.ProgressStateEntity;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.ui.api.ProgressControl;

public class MockProgressControl implements ProgressControl {
    private double percentage = 0.;
    private String message = "";

    public MockProgressControl(String textToShow) {
        System.out.println(textToShow);
    }

    @Override
    public void setProgressPercentage(double value) {
        percentage = value;
        reportStatus();
    }

    private void reportStatus() {
        System.out.println(format("[%.2f] - %s", percentage, message));
    }

    @Override
    public void setText(String text) {
        message = text;
        reportStatus();
    }

    @Override
    public void update(Observable observable, ProgressStateEntity argument) {
        setText(argument.getProgressText());
        setProgressPercentage(argument.getPercent());
    }
}
