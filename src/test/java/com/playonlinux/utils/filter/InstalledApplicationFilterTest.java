/*
 * Copyright (C) 2015 Kaspar Tint
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

package com.playonlinux.utils.filter;

import com.playonlinux.dto.ui.InstalledApplicationDTO;
import com.playonlinux.utils.observer.Observable;
import com.playonlinux.utils.observer.Observer;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class InstalledApplicationFilterTest {

    private MockFilterObserver mockFilterObserver;

    @Before
    public void setUp() throws MalformedURLException {
        mockFilterObserver = new MockFilterObserver();
    }

    @Test
    public void testFilterWithString() {
        mockFilterObserver.setName("St");
        assertEquals(1, mockFilterObserver.getFilteredInstalledApplications().size());
    }

    @Test
    public void testFilterWithEmptyString() {
        mockFilterObserver.setName("");
        assertEquals(1, mockFilterObserver.getFilteredInstalledApplications().size());
    }

    @Test
    public void testFilterWithWrongString() {
        mockFilterObserver.setName("WRONG");
        assertEquals(0, mockFilterObserver.getFilteredInstalledApplications().size());
    }

    public class MockFilterObserver implements Observer {

        private final InstalledApplicationFilter filter = new InstalledApplicationFilter();
        private final List<InstalledApplicationDTO> installedApplications;
        private List<InstalledApplicationDTO> filteredInstalledApplications;

        MockFilterObserver() throws MalformedURLException{
            installedApplications = new ArrayList<>();
            installedApplications.add(new InstalledApplicationDTO.Builder()
                    .withName("Steam")
                    .withIcon(new URL("http://store.steampowered.com/"))
                    .build());

            filter.addObserver(this);
        }

        @Override
        public void update(Observable observable, Object o) {
            filteredInstalledApplications = installedApplications.stream().filter(filter::apply).collect(Collectors.toList());
        }

        public void setName(String name) {
            filter.setName(name);
        }

        public List<InstalledApplicationDTO> getFilteredInstalledApplications() {
            return this.filteredInstalledApplications;
        }
    }
}
