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

package com.playonlinux.ui.impl.javafx.consolewindow;


import com.playonlinux.injection.Inject;
import com.playonlinux.injection.Scan;
import com.playonlinux.installer.CancelException;
import com.playonlinux.messages.Message;
import com.playonlinux.messages.RunnableWithParameter;
import com.playonlinux.messages.SynchronousMessage;
import com.playonlinux.ui.api.CommandInterpreter;
import com.playonlinux.ui.api.CommandInterpreterFactory;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.common.PlayOnLinuxScene;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.lang.exception.ExceptionUtils;

import static com.playonlinux.lang.Localisation.translate;

@Scan
public class ConsoleWindow extends Stage implements PlayOnLinuxWindow {

    @Inject
    private static CommandInterpreterFactory commandInterpreterFactory;

    CommandInterpreter commandInterpreter = commandInterpreterFactory.createInstance();

    public ConsoleWindow() {
        VBox rootPane = new VBox();

        TextField command = new TextField();
        TextArea console = new TextArea();
        console.setEditable(false);
        rootPane.getChildren().addAll(console, command);

        Scene scene = new PlayOnLinuxScene(rootPane);

        this.setScene(scene);
        this.setTitle(translate("${application.name} console"));
        this.show();

        command.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                final String commandTonSend = command.getText();
                command.setDisable(true);
                console.appendText(">>> " + commandTonSend + "\n");
                command.setText("");
                commandInterpreter.sendCommand(commandTonSend, message -> {
                    Platform.runLater(() -> {
                        console.appendText(message);
                        command.setDisable(false);
                    });
                });

            }
        });

        this.setOnCloseRequest(event -> commandInterpreter.close());
    }
}
