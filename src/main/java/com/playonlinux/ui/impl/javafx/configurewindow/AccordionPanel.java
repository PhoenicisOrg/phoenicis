package com.playonlinux.ui.impl.javafx.configurewindow;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;


public class AccordionPanel extends TitledPane {
    public AccordionPanel(String name) {
        this.setText(name);
        this.setContent(new PaneContent());
    }

    private class PaneContent extends GridPane {

    }
}
