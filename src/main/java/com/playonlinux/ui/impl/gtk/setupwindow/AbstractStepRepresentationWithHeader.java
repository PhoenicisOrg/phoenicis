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

package com.playonlinux.ui.impl.gtk.setupwindow;

import com.playonlinux.core.messages.CancelerMessage;
import org.apache.log4j.Logger;
import org.gnome.gdk.Pixbuf;
import org.gnome.gdk.RGBA;
import org.gnome.gtk.*;

import java.io.IOException;


abstract class AbstractStepRepresentationWithHeader extends AbstractStepRepresentation {
    private static final Logger LOGGER = Logger.getLogger(AbstractStepRepresentationWithHeader.class);
    Fixed contentPanel;

    AbstractStepRepresentationWithHeader(SetupWindowGTKImplementation parent, CancelerMessage messageWaitingForResponse) {
        super(parent, messageWaitingForResponse);
    }


    public void addToContentPanel(Widget contentToAdd, int x, int y) {
        this.contentPanel.put(contentToAdd, x, y);
    }
    /**
     * Draw the header at the top of the window
     */
    private void drawHeader() {
        String title = this.getParentWizardTitle(); // FIXME: use this variable to draw the title of the window
        Layout header = new Layout();
        header.overrideBackground(StateFlags.NORMAL, RGBA.WHITE);
        header.setSizeRequest(522, 65);
        header.setBorderWidth(1);

        try {
            Pixbuf pixbuf = new Pixbuf(this.createTopImage());
            Image topImage = new Image(pixbuf);
            header.put(topImage, 456, 0);
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        this.addToStep(header, 0, 0);
    }

    private void drawPanelForTopHeader() {
        Fixed panel = new Fixed();
        panel.setSizeRequest(522, 294);
        this.addToStep(panel, 0, 64);
        this.contentPanel = panel;
    }

    private byte[] createTopImage() {
        return this.getParentTopImage();
    }

    public void installStep() {
        this.clearAllOnParent();
        this.drawHeader();
        this.drawPanelForTopHeader();
        this.drawFooter();

        this.setStepEvents();
        this.drawStepContent();
    }


}
