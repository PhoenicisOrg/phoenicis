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
import com.playonlinux.common.dto.ui.InstalledApplicationDTO;
import org.apache.commons.lang.StringUtils;

import java.net.URL;
import java.util.Observable;

/**
 * Filter for installed applications in the MainWindow
 */
public class InstalledApplicationFilter extends Observable implements Filter<InstalledApplicationDTO> {

    private boolean transaction = false;

    private String name = "";
    private URL icon = null;

    @Override
    public void startTransaction() { transaction = true; }

    @Override
    public void endTransaction(boolean fire) {
        transaction = false;
        if(fire){
            this.fireUpdate();
        }
    }

    @Override
    public boolean apply(InstalledApplicationDTO item) {
        // We want to return the whole list for empty search string. Otherwise compare strings.
        return !StringUtils.isNotBlank(name) || item.getName().toLowerCase().contains(name);
    }

    private void fireUpdate() {
        if(!transaction){
            this.setChanged();
            this.notifyObservers();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase();
        fireUpdate();
    }

    public URL getIcon() {
        return icon;
    }

    public void setIcon(URL icon) {
        this.icon = icon;
        fireUpdate();
    }
}
