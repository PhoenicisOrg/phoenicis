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

package com.playonlinux.ui.impl.javafx.mainwindow.console;

import java.util.ArrayList;
import java.util.List;

/**
 * Small helper intended for keeping track of commands that have been entered into the console window.
 */
public class CommandHistory {

    private List<Item> history = new ArrayList<>();
    private int historyPosition = 0;

    public void add(Item item){
        this.history.add(item);
        historyPosition = this.history.size();
    }

    public Item up(){
        if(historyPosition > 0){
            historyPosition--;
        }
        return current();
    }

    public Item down(){
        historyPosition = (historyPosition < history.size()) ? (historyPosition + 1) : history.size();
        return current();
    }

    public Item current(){
        if(history.size() > 0 && historyPosition < history.size()){
            return history.get(historyPosition);
        }
        return Item.empty;
    }


    public static class Item {

        public static Item empty = new Item("", 0);

        private String command;
        private int cursorPosition;

        public Item(String command, int cursorPosition){
            this.command = command;
            this.cursorPosition = cursorPosition;
        }

        public String getCommand(){ return command; }
        public int getCursorPosition(){ return cursorPosition; }

    }

}
