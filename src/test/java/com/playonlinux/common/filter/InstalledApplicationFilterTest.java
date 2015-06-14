package com.playonlinux.common.filter;

import com.playonlinux.common.dto.ui.InstalledApplicationDTO;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

public class InstalledApplicationFilterTest {

    private MockFilterObserver mockFilterObserver;

    @Before
    public void setUp() throws MalformedURLException {
        mockFilterObserver = new MockFilterObserver();
    }

    @Test
    public void testFilterWithString() {
        mockFilterObserver.setName("St");
        assert mockFilterObserver.getFilteredInstalledApplications().size() == 1;
    }

    @Test
    public void testFilterWithEmptyString() {
        mockFilterObserver.setName("");
        assert mockFilterObserver.getFilteredInstalledApplications().size() == 1;
    }

    @Test
    public void testFilterWithWrongString() {
        mockFilterObserver.setName("WRONG");
        assert mockFilterObserver.getFilteredInstalledApplications().size() == 0;
    }

    public class MockFilterObserver implements Observer {

        private final InstalledApplicationFilter filter = new InstalledApplicationFilter();
        private List<InstalledApplicationDTO> installedApplications;
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

        public void setName(String name) { filter.setName(name); }

        public List<InstalledApplicationDTO> getFilteredInstalledApplications() { return this.filteredInstalledApplications; }
    }
}
