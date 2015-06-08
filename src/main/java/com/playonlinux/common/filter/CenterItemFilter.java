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
import com.playonlinux.common.dto.ui.CenterItemDTO;
import com.playonlinux.common.dto.web.ScriptDTO;

import java.util.Observable;

/**
 * Filter implementation for CenterItems
 */
public class CenterItemFilter extends Observable implements Filter<CenterItemDTO> {

    private String title = null;
    private String category = null;
    private boolean showTesting = false;
    private boolean showNoCd = false;
    private boolean showCommercial = true;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.notifyObservers();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        this.notifyObservers();
    }

    public boolean isShowTesting() {
        return showTesting;
    }

    public void setShowTesting(boolean showTesting) {
        this.showTesting = showTesting;
        this.notifyObservers();
    }

    public boolean isShowNoCd() {
        return showNoCd;
    }

    public void setShowNoCd(boolean showNoCd) {
        this.showNoCd = showNoCd;
        this.notifyObservers();
    }

    public boolean isShowCommercial() {
        return showCommercial;
    }

    public void setShowCommercial(boolean showCommercial) {
        this.showCommercial = showCommercial;
        this.notifyObservers();
    }


    @Override
    public boolean apply(CenterItemDTO item) {
        if (category != null && item.getCategoryName() != category) {
            return false;
        }
        if (title != null && !item.getName().contains(title)) {
            return false;
        }

        if (item.isTesting() && !showTesting) {
            return false;
        }
        if (item.isRequiresNoCd() && !showNoCd) {
            return false;
        }
        return !(item.isCommercial() && !showCommercial);

    }


    private void fireUpdate() {
        this.setChanged();
        this.notifyObservers();
    }

}
