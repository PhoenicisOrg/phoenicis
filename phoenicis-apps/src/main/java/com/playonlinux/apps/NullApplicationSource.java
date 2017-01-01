package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.Collections;
import java.util.List;

class NullApplicationSource implements ApplicationsSource {
    @Override
    public List<CategoryDTO> fetchInstallableApplications() {
        return Collections.emptyList();
    }
}
