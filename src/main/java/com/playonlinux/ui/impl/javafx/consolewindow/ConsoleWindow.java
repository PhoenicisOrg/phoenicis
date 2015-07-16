/*
 * Copyright (C) 2015 PÂRIS Quentin
 * Copyright (C) 2015 Markus Ebner
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


import com.playonlinux.core.injection.Inject;
import com.playonlinux.core.injection.Scan;
import com.playonlinux.core.python.CommandInterpreter;
import com.playonlinux.core.python.CommandInterpreterException;
import com.playonlinux.ui.api.CommandInterpreterFactory;
import com.playonlinux.ui.api.PlayOnLinuxWindow;
import com.playonlinux.ui.impl.javafx.common.PlayOnLinuxScene;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.playonlinux.core.lang.Localisation.translate;

@Scan
public class ConsoleWindow extends Stage implements PlayOnLinuxWindow {

    private static final String NOT_INSIDE_BLOCK = ">>> ";
    private static final String INSIDE_BLOCK = "... ";

    private List<String> commandHistory = new ArrayList<>();
    private int historyIndex = 0;

    @Inject
    private static CommandInterpreterFactory commandInterpreterFactory;

    final CommandInterpreter commandInterpreter;

    private String nextSymbol = NOT_INSIDE_BLOCK;

    public ConsoleWindow() throws CommandInterpreterException {
        commandInterpreter = commandInterpreterFactory.createInstance();
        final VBox rootPane = new VBox();

        final TextField command = new TextField();
        command.getStyleClass().add("consoleCommandType");
        final TextFlow console = new TextFlow();
        final ScrollPane consolePane = new ScrollPane(console);
        rootPane.getStyleClass().add("consoleWindow");
        consolePane.getStyleClass().add("console");
        consolePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        rootPane.getChildren().addAll(consolePane, command);

        Scene scene = new PlayOnLinuxScene(rootPane);

        this.setScene(scene);
        this.setTitle(translate("${application.name} console"));
        this.show();

        command.requestFocus();

        command.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                final String commandToSend = command.getText();
                command.setDisable(true);
                commandHistory.add(commandToSend);
                historyIndex++;
                Text commandText = new Text(nextSymbol + commandToSend + "\n");
                commandText.getStyleClass().add("commandText");
                console.getChildren().add(commandText);
                command.setText("");

                if (commandInterpreter.sendLine(commandToSend, message -> Platform.runLater(() -> {
                    if (!StringUtils.isBlank(message)) {
                        Text resultText = new Text(message);
                        resultText.getStyleClass().add("resultText");
                        console.getChildren().add(resultText);
                    }
                    command.setDisable(false);
                    command.requestFocus();
                    consolePane.setVvalue(consolePane.getVmax());
                }))) {
                    nextSymbol = NOT_INSIDE_BLOCK;
                } else {
                    nextSymbol = INSIDE_BLOCK;
                }
            }else if(event.getCode() == KeyCode.UP && historyIndex > 0){
                historyIndex--;
                System.out.println("HistoryIndex: " + historyIndex);
                command.setText(commandHistory.get(historyIndex));
            }else if(event.getCode() == KeyCode.DOWN){
                historyIndex++;
                if(historyIndex == commandHistory.size()){
                    command.setText("");
                }else if(historyIndex < commandHistory.size()){
                    System.out.println("HistoryIndex: " + historyIndex);
                    command.setText(commandHistory.get(historyIndex));
                }else {
                    historyIndex = commandHistory.size();
                }
            }
        });

        this.setOnCloseRequest(event -> commandInterpreter.close());
    }
}
