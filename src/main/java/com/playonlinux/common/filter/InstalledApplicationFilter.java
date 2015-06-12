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
package com.playonlinux.common.filter;

import com.playonlinux.common.api.filter.Filter;
import com.playonlinux.common.dto.ui.ShortcutDTO;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.Observable;

public class InstalledApplicationFilter extends Observable implements Filter<ShortcutDTO> {

    private boolean transaction = false;

    private String name = "";
    private URL icon;

    @Override
    public void startTransaction() { transaction = true; }

    @Override
    public void endTransaction(boolean fire) {
        transaction = false;
        if(fire){
            this.fireUpdate();
        }
    }

    private void fireUpdate() {
        if(!transaction){
            this.setChanged();
            this.notifyObservers();
        }
    }

    @Override
    public boolean apply(ShortcutDTO item) {
        if(StringUtils.isBlank(name)) {
            return false;
        }

        if(StringUtils.isNotBlank(name)){
            if(!item.getName().toLowerCase().contains(name)) {
                return false;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getIcon() {
        return icon;
    }

    public void setIcon(URL icon) {
        this.icon = icon;
    }
}
