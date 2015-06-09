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
import javafx.application.Platform;
import org.apache.commons.lang.StringUtils;

import java.util.Observable;

/**
 * Filter implementation for CenterItems
 */
public class CenterItemFilter extends Observable implements Filter<CenterItemDTO> {

    private boolean transaction = false;

    private String title = "";
    private String category = null;
    private boolean showTesting = false;
    private boolean showNoCd = false;
    private boolean showCommercial = true;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.toLowerCase();
        this.fireUpdate();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        this.fireUpdate();
    }

    public boolean isShowTesting() {
        return showTesting;
    }

    public void setShowTesting(boolean showTesting) {
        this.showTesting = showTesting;
        this.fireUpdate();
    }

    public boolean isShowNoCd() {
        return showNoCd;
    }

    public void setShowNoCd(boolean showNoCd) {
        this.showNoCd = showNoCd;
        this.fireUpdate();
    }

    public boolean isShowCommercial() {
        return showCommercial;
    }

    public void setShowCommercial(boolean showCommercial) {
        this.showCommercial = showCommercial;
    }

    @Override
    public boolean apply(CenterItemDTO item) {
        if(StringUtils.isBlank(title) && category == null) {
            return false;
        }

        if(StringUtils.isNotBlank(title)){
            if(!item.getName().toLowerCase().contains(title)) {
                return false;
            }
        } else if(category != null && !category.equals(item.getCategoryName())) {
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




    @Override
    public void startTransaction() {
        transaction = true;
    }

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

}
