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

package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.containers.VirtualDriveDTO;
import com.playonlinux.core.observer.Observable;
import com.playonlinux.core.observer.Observer;
import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.Iterator;


public class AccordionWidget extends Accordion implements Observer {
    AccordionPanel expandedPane;

    public AccordionWidget() {
        super();

        this.expandedPaneProperty().addListener((property, oldPane, newPane) -> {
            if (oldPane != null) oldPane.setCollapsible(true);
            if (newPane != null) Platform.runLater(() -> newPane.setCollapsible(false));
        });


    }

    @Override
    public void update(Observable o, Object arg) {
        final String expandedPaneName;
        if(expandedPane != null) {
            expandedPaneName = expandedPane.getText();
        } else {
            expandedPaneName = null;
        }

        this.getPanes().clear();

        Platform.runLater(() -> {
            assert(o instanceof Iterator);
            Iterable<VirtualDriveDTO> virtualdrives = (Iterable<VirtualDriveDTO>) o;
            int i = 0;
            for (VirtualDriveDTO virtualdrive : virtualdrives) {
                this.getPanes().add(new AccordionPanel(virtualdrive.getName()));
                i++;
            }

            if (i > 0) {
                if (expandedPaneName == null) {
                    this.setExpandedPane(this.getPanes().get(0));
                } else {
                    TitledPane paneToExpand = this.getPaneFromName(expandedPaneName);
                    if(paneToExpand == null) {
                        this.setExpandedPane(this.getPanes().get(0));
                    } else {
                        this.setExpandedPane(paneToExpand);
                    }

                }
            }
        });
    }

    private TitledPane getPaneFromName(String expandedPaneName) {
        for(TitledPane titledPane: this.getPanes()) {
            if(expandedPaneName.equals(titledPane.getText())) {
                return expandedPane;
            }
        }
        return null;
    }
}
