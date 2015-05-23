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

package com.playonlinux.ui.api;

import com.playonlinux.common.dtos.ScriptDTO;
import com.playonlinux.common.dtos.ScriptInformationsDTO;
import com.playonlinux.domain.PlayOnLinuxError;
import com.playonlinux.utils.OperatingSystem;
import org.apache.commons.lang.StringUtils;

import java.util.Observable;

/**
 * This class is handling the filtering of Installers, providing one central place for
 * changing how the filtering is done.
 * InstallerFilters extends Observable, so that changes get directly promoted and
 * initiate a new filtering process.
 */
public class InstallerFilter extends Observable {

    private String title = null;
    private String category = null;
    private boolean showTesting = false;
    private boolean showNoCd = false;
    private boolean showCommercial = true;

    private final OperatingSystem hostOs;


    public InstallerFilter() {
        //TODO: Pre-define filters.
        OperatingSystem os;
        try {
            os = OperatingSystem.fetchCurrentOperationSystem();
        } catch (PlayOnLinuxError err) {
            os = null;
        }
        this.hostOs = os;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.fireUpdate();
    }


    /**
     * Test the given script against the filter rules.
     * <p>
     * Note: At the moment, this method does not filter categories, since Scripts don't know about their own category.
     * </p>
     *
     * @param script Script which should be tested against the filter.
     * @return True if this Script matches the filter criteria, false otherwise.
     */
    public boolean apply(ScriptDTO script) {
        //TODO: write testcases for this method.
        ScriptInformationsDTO scriptInfo = script.getScriptInformations();
        boolean isTesting = scriptInfo.getTestingOperatingSystems().contains(hostOs);

        if (isTesting && !showTesting) {
            return false;
        }
        if (scriptInfo.isRequiresNoCD() && !showNoCd) {
            return false;
        }
        if (!scriptInfo.isFree() && !showCommercial) {
            return false;
        }
        if (StringUtils.isNotBlank(title)) {
            if (!script.getName().contains(title)) {
                return false;
            }
        }

        return true;
    }


    private void fireUpdate() {
        this.setChanged();
        this.notifyObservers();
    }

}
