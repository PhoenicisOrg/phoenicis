package com.playonlinux.apps;

import com.playonlinux.apps.dto.CategoryDTO;

import java.util.List;

public interface AppsManager {
    List<CategoryDTO> fetchInstallableApplications();
}
