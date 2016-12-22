/*
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

import java.util.ArrayList;
import java.util.List;

/**
 * Small helper intended for keeping track of commands that have been entered into the console window.
 */
class CommandHistory {

    private final List<Item> history = new ArrayList<>();
    private int historyPosition = 0;

    public void add(Item item) {
        this.history.add(item);
        historyPosition = this.history.size();
    }

    Item up() {
        if (historyPosition > 0) {
            historyPosition--;
        }
        return current();
    }

    Item down() {
        historyPosition = historyPosition < history.size() ? historyPosition + 1 : history.size();
        return current();
    }

    private Item current() {
        if (!history.isEmpty() && historyPosition < history.size()) {
            return history.get(historyPosition);
        }
        return Item.EMPTY;
    }


    static final class Item {
        static final Item EMPTY = new Item("", 0);

        private final String command;
        private final int cursorPosition;

        Item(String command, int cursorPosition) {
            this.command = command;
            this.cursorPosition = cursorPosition;
        }

        String getCommand() {
            return command;
        }

        int getCursorPosition() {
            return cursorPosition;
        }

    }

}
