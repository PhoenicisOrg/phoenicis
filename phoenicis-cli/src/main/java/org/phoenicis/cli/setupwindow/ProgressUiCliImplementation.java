/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.cli.setupwindow;

import org.phoenicis.scripts.ui.Message;
import org.phoenicis.scripts.ui.ProgressControl;
import org.phoenicis.scripts.ui.ProgressUi;

import static java.lang.Math.max;
import static java.lang.Math.min;

class ProgressUiCliImplementation implements ProgressUi {
    private final String title;
    private final boolean interactive;
    private final boolean verbose;

    ProgressUiCliImplementation(String title, boolean interactive, boolean verbose) {
        this.title = title;
        this.interactive = interactive;
        this.verbose = verbose;

        printIfVerbose(title);
        printIfVerbose("-----------------");
    }

    @Override
    public void showProgressBar(Message<ProgressControl> message, String textToShow) {
        printIfVerbose(textToShow);

        message.send(new ProgressControl() {
            private double percentage = 0;
            private String text = "";

            @Override
            public void setProgressPercentage(double value) {
                percentage = min(100, max(0, value));
                printIfVerbose("[" + String.format("%.2f", percentage) + "] " + textToShow + " : " + text);
            }

            @Override
            public void setText(String text) {
                this.text = text;
                printIfVerbose("[" + String.format("%.2f", percentage) + "] " + textToShow + " : " + text);
            }
        });
    }
    @Override
    public void close() {
        // Do nothing
    }

    private void printIfVerbose(String textToShow) {
        if (verbose) {
            System.out.println(textToShow);
        }
    }
}
