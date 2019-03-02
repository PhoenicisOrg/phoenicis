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
import org.phoenicis.javafx.components.application.control.ApplicationsFeaturePanel;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.List;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class AppsController {
    private final ApplicationsFeaturePanel view;

    private Runnable onAppLoaded = () -> {
    };

    public AppsController(ApplicationsFeaturePanel view, RepositoryManager repositoryManager) {
        this.view = view;

        repositoryManager.addCallbacks(this::populate, this::showError);
    }

    public void setOnAppLoaded(Runnable onAppLoaded) {
        this.onAppLoaded = onAppLoaded;
    }

    public ApplicationsFeaturePanel getView() {
        return view;
    }

    private void populate(RepositoryDTO repositoryDTO) {
        Platform.runLater(() -> {
            final List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().get(0).getCategories();

            getView().getCategories().setAll(categoryDTOS);

            getView().setInitialized(true);

            // TODO: remove the callback (I don't think it is changed by `setOnAppLoaded` before it is called here)
            onAppLoaded.run();
        });
    }

    private void showError(Exception e) {
        Platform.runLater(() -> {
            final ErrorDialog errorDialog = ErrorDialog.builder()
                    .withOwner(view.getScene().getWindow())
                    .withException(e)
                    .withMessage(
                            tr("Connecting to the repository failed.\nPlease check your connection and try again."))
                    .build();

            errorDialog.showAndWait();
        });
    }
}
