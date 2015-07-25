/*
 * Copyright (C) 2015 Markus Ebner
 *               2015 PÂRIS Quentin
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

package com.playonlinux.apps;

import com.playonlinux.apps.entities.AppsItemEntity;
import com.playonlinux.core.filter.Filter;
import com.playonlinux.core.observer.AbstractObservableImplementation;
import org.apache.commons.lang.StringUtils;

/**
 * Filter implementation for CenterItems
 */
public class AppsFilter extends AbstractObservableImplementation implements Filter<AppsItemEntity> {
    private final String title;
    private final String category;

    public AppsFilter(String category,
                      String title,
                      boolean showTesting,
                      boolean showNoCd,
                      boolean showCommercial) {

        this.category = category;
        this.title = title;
        this.showTesting = showTesting;
        this.showNoCd = showNoCd;
        this.showCommercial = showCommercial;
    }

    private final boolean showTesting;
    private final boolean showNoCd;
    private final boolean showCommercial;

    @Override
    public boolean apply(AppsItemEntity item) {
        if(StringUtils.isBlank(title) && category == null) {
            return false;
        }

        if(StringUtils.isNotBlank(title)){
            if(!item.getName().toLowerCase().contains(title.toLowerCase())) {
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


}
