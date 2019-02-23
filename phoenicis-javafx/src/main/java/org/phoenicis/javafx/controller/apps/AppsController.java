/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
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

package org.phoenicis.javafx.controller.apps;

import javafx.application.Platform;
import org.phoenicis.javafx.views.mainwindow.apps.ApplicationsView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.List;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class AppsController {
    private final ApplicationsView view;
    private final RepositoryManager repositoryManager;

    private boolean firstViewSelection = true;

    private Runnable onAppLoaded = () -> {
    };

    public AppsController(ApplicationsView view, RepositoryManager repositoryManager) {
        this.view = view;
        this.repositoryManager = repositoryManager;

        this.view.setOnSelectionChanged(event -> {
            if (this.view.isSelected() && this.firstViewSelection) {
                this.repositoryManager.addCallbacks(this::populateView,
                        e -> Platform.runLater(() -> view.showFailure(
                                tr("Connecting to the repository failed.\nPlease check your connection and try again."),
                                Optional.of(e))));

                loadApps();

                this.firstViewSelection = false;
            }
        });
    }

    private void loadApps() {
        this.view.showWait();
        this.repositoryManager.triggerCallbacks();

        this.view.setOnRetryButtonClicked(event -> {
            this.view.showWait();
            this.repositoryManager.triggerRepositoryChange();
        });

        onAppLoaded.run();
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ApplicationsView getView() {
        return view;
    }

    private void populateView(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();
            this.view.populate(categoryDTOS);
        });
    }
}
