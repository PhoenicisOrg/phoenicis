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

package com.playonlinux.javafx.views.mainwindow.console;


import com.playonlinux.scripts.interpreter.ScriptInterpreter;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.function.Consumer;

import static com.playonlinux.configuration.localisation.Localisation.translate;

public class ConsoleTab extends Tab {
    private final CommandHistory commandHistory = new CommandHistory();
    private final TextField command = new TextField();
    private final TextFlow console = new TextFlow();
    private Consumer<String> onSendCommand = text -> {};

    public ConsoleTab() {
        final VBox content = new VBox();

        this.setText(translate("Console"));
        this.setContent(content);

        command.getStyleClass().add("consoleCommandType");
        final ScrollPane consolePane = new ScrollPane(console);
        content.getStyleClass().add("rightPane");

        consolePane.getStyleClass().add("console");
        consolePane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        content.getChildren().addAll(consolePane, command);

        command.requestFocus();

        command.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.onSendCommand.accept(command.getText());
                commandHistory.add(new CommandHistory.Item(command.getText(), command.getCaretPosition()));
                clearCommand();
            }
        });

        command.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP) {
                CommandHistory.Item historyItem = commandHistory.up();
                command.setText(historyItem.getCommand());
                command.positionCaret(historyItem.getCursorPosition());
            } else if (event.getCode() == KeyCode.DOWN) {
                CommandHistory.Item historyItem = commandHistory.down();
                command.setText(historyItem.getCommand());
                command.positionCaret(historyItem.getCursorPosition());
            }
        });
    }

    public void setOnSendCommand(Consumer<String> onSendCommand) {
        this.onSendCommand = onSendCommand;
    }

    public void disableCommand() {
        command.setDisable(true);
    }

    public void enableCommand() {
        Platform.runLater(() -> {
            command.setDisable(false);
            command.requestFocus();
        });
    }

    public void clearCommand() {
        command.setText("");
    }

    public String getCommandValue() {
        return command.getText();
    }

    public void appendTextToConsole(String text) {
        appendTextToConsole(text, "#999999");
    }

    public void appendTextToConsole(String text, String color) {
        Text commandText = new Text(text);
        commandText.setStyle("-fx-fill: " + color);
        Platform.runLater(() -> {
            console.getChildren().add(commandText);
        });
    }


}
