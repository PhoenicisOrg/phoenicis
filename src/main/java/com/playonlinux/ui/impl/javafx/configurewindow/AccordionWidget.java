package com.playonlinux.ui.impl.javafx.configurewindow;

import com.playonlinux.ui.dtos.VirtualDriveDTO;
import javafx.application.Platform;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

import java.util.Observable;
import java.util.Observer;

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
