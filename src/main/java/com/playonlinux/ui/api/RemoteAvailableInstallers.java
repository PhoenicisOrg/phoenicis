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

package com.playonlinux.ui.api;

import com.playonlinux.common.dtos.CategoryDTO;
import com.playonlinux.common.dtos.ScriptDTO;
import com.playonlinux.common.dtos.ScriptInformationsDTO;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.utils.OperatingSystem;

import java.util.Observable;
import java.util.Observer;

public interface RemoteAvailableInstallers extends Iterable<CategoryDTO>, Filterable<ScriptDTO> {
    void addObserver(Observer o);

    int getNumberOfCategories();

    boolean isUpdating();

    boolean hasFailed();

    Iterable<ScriptDTO> getList();

    InstallerFilter getFilter();

    ScriptDTO getByName(String scriptName) throws PlayOnLinuxError;

    void refresh();


    /**
     * This class is handling the filtering of Installers, providing one central place for
     * changing how the filtering is done.
     * InstallerFilters extends Observable, so that changes get directly promoted and
     * initiate a new filtering process.
     */
    abstract class InstallerFilter extends Observable implements Filter<ScriptDTO> {

        protected String title;
        protected String category;
        protected boolean showTesting;
        protected boolean showNoCd;
        protected boolean showCommercial;

        public String getTitle() { return title; }
        public void setTitle(String title) {
            //prevent unnecessary filtering
            if(this.title != title){
                this.title = title;
                this.fireUpdate();
            }
        }

        public String getCategory() { return category; }
        public void setCategory(String category) {
            this.category = category;
            this.fireUpdate();
        }

        public boolean isShowTesting() { return showTesting; }
        public void setShowTesting(boolean showTesting) {
            this.showTesting = showTesting;
            this.fireUpdate();
        }

        public boolean isShowNoCd() { return showNoCd; }
        public void setShowNoCd(boolean showNoCd) {
            this.showNoCd = showNoCd;
            this.fireUpdate();
        }

        public boolean isShowCommercial() { return showCommercial; }
        public void setShowCommercial(boolean showCommercial) {
            this.showCommercial = showCommercial;
            this.fireUpdate();
        }

        private void fireUpdate(){
            this.setChanged();
            this.notifyObservers();
        }

    }

}
