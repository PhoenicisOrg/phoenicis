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

package org.phoenicis.javafx.controller.engines;

import com.google.common.collect.ImmutableMap;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.phoenicis.engines.Engine;
import org.phoenicis.engines.EnginesManager;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.javafx.controller.apps.AppsController;
import org.phoenicis.javafx.dialogs.ErrorDialog;
import org.phoenicis.javafx.dialogs.SimpleConfirmDialog;
import org.phoenicis.javafx.themes.ThemeManager;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.RepositoryDTO;
import org.phoenicis.tools.archive.Extractor;
import org.phoenicis.tools.http.Downloader;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class EnginesController {
    private final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AppsController.class);
    private final EnginesView enginesView;
    private final RepositoryManager repositoryManager;
    private final EnginesManager enginesManager;
    private final Downloader downloader;
    private final Extractor extractor;

    private final ThemeManager themeManager;
    private RepositoryDTO repositoryCache;
    private Map<String, Engine> enginesCache = new HashMap<>();
    private final Map<String, List<EngineSubCategoryDTO>> versionsCache = new HashMap<>();

    private boolean firstViewSelection = true;

    public EnginesController(EnginesView enginesView, RepositoryManager repositoryManager,
            EnginesManager enginesManager, ThemeManager themeManager, Downloader downloader, Extractor extractor) {
        super();

        this.enginesView = enginesView;
        this.repositoryManager = repositoryManager;
        this.enginesManager = enginesManager;
        this.themeManager = themeManager;
        this.downloader = downloader;
        this.extractor = extractor;

        this.enginesView.setOnSelectEngineCategory(engineCategoryDTO -> {
            // TODO: better way to get engine ID
            final String engineId = engineCategoryDTO.getName().toLowerCase();
            // only if not cached
            if (!this.versionsCache.containsKey(engineId)) {
                this.enginesManager.fetchAvailableVersions(engineId,
                        versions -> {
                            this.versionsCache.put(engineId, versions);
                            this.enginesView.updateVersions(engineCategoryDTO, versions);
                        },
                        e -> Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withException(e)
                                    .withOwner(this.enginesView.getContent().getScene().getWindow())
                                    .build();

                            errorDialog.showAndWait();
                        }));
            }
        });

        this.enginesView.setOnInstallEngine(engineDTO -> {
            final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                    .withTitle(tr("Install {0}", engineDTO.getVersion()))
                    .withMessage(tr("Are you sure you want to install {0}?", engineDTO.getVersion()))
                    .withOwner(enginesView.getContent().getScene().getWindow())
                    .withResizable(true)
                    .withYesCallback(() -> this.enginesManager.getEngine(engineDTO.getId(), engine -> {
                        engine.install(engineDTO.getSubCategory(), engineDTO.getVersion());

                        // invalidate cache and force view update to show installed version correctly
                        this.versionsCache.remove(engineDTO.getId());
                        this.forceViewUpdate();
                    }, e -> Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error"))
                                .withException(e)
                                .withOwner(this.enginesView.getContent().getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    })))
                    .build();

            confirmMessage.showAndCallback();
        });

        this.enginesView.setOnInstallEngineFromUrl(this::showInstallFromUrlDialog);

        this.enginesView.setOnDeleteEngine(engineDTO -> {
            final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                    .withTitle(tr("Delete {0}", engineDTO.getVersion()))
                    .withMessage(tr("Are you sure you want to delete {0}?", engineDTO.getVersion()))
                    .withOwner(enginesView.getContent().getScene().getWindow())
                    .withResizable(true)
                    .withYesCallback(() -> this.enginesManager.getEngine(engineDTO.getId(), engine -> {
                        engine.delete(engineDTO.getSubCategory(), engineDTO.getVersion());

                        // invalidate cache and force view update to show deleted version correctly
                        this.versionsCache.remove(engineDTO.getId());
                        this.forceViewUpdate();
                    }, e -> Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error"))
                                .withException(e)
                                .withOwner(this.enginesView.getContent().getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    })))
                    .build();

            confirmMessage.showAndCallback();
        });

        this.enginesView.setOnSelectionChanged(event -> {
            if (this.enginesView.isSelected() && this.firstViewSelection) {
                this.repositoryManager.addCallbacks(
                        repositoryDTO -> this.enginesManager.fetchAvailableEngines(
                                repositoryDTO,
                                engines -> this.populateView(repositoryDTO, engines),
                                e -> Platform.runLater(
                                        () -> enginesView.showFailure(tr("Loading engines failed."), Optional.of(e)))),
                        e -> Platform.runLater(
                                () -> enginesView.showFailure(tr("Loading engines failed."), Optional.of(e))));

                this.repositoryManager.triggerCallbacks();

                this.firstViewSelection = false;
            }
        });
    }

    public EnginesView getView() {
        return enginesView;
    }

    private void populateView(RepositoryDTO repositoryDTO, Map<String, Engine> engines) {
        this.repositoryCache = repositoryDTO;
        this.enginesCache = engines;

        // show a waiting screen until the engines are loaded
        Platform.runLater(enginesView::showWait);

        // fetch all categories consisting of engines that are contained in the repository
        final List<CategoryDTO> categoryDTOS = repositoryDTO.getTypes().stream()
                .filter(type -> "engines".equals(type.getId()))
                .flatMap(type -> type.getCategories().stream())
                .collect(Collectors.toList());

        Platform.runLater(() ->
        // generate the necessary css for the engine categories
        setDefaultEngineIcons(categoryDTOS));

        // fetch the engine categories objects contained in the engine categories
        final Queue<EngineCategoryDTO> engineCategories = new ArrayDeque<>(
                enginesManager.getAvailableEngines(categoryDTOS));

        // insert the missing engine subcategories into the engine categories
        fetchEngineSubcategories(engineCategories, ImmutableMap.of(), subcategoryMap -> {
            final List<EngineCategoryDTO> categories = subcategoryMap.entrySet().stream()
                    .map(entry -> new EngineCategoryDTO.Builder(entry.getKey())
                            .withSubCategories(entry.getValue())
                            .build())
                    .collect(Collectors.toList());

            Platform.runLater(() ->
            // update the view
            enginesView.populate(categories, engines));
        });
    }

    /**
     * Fetches all engine subcategories that belong to a given list of engine categories
     *
     * @param engineCategories The engine categories
     * @param result The temporary transport variable
     * @param callback A callback method, which is called after all engine subcategories have been fetched
     */
    private void fetchEngineSubcategories(Queue<EngineCategoryDTO> engineCategories,
            Map<EngineCategoryDTO, List<EngineSubCategoryDTO>> result,
            Consumer<Map<EngineCategoryDTO, List<EngineSubCategoryDTO>>> callback) {
        final Queue<EngineCategoryDTO> queue = new ArrayDeque<>(engineCategories);

        if (queue.isEmpty()) {
            // recursion anchor
            callback.accept(result);
        } else {
            final EngineCategoryDTO engineCategory = queue.poll();
            final String engineId = engineCategory.getName().toLowerCase();

            enginesManager.fetchAvailableVersions(
                    engineId,
                    versions ->
                    // recursively process the remaining engine categories
                    fetchEngineSubcategories(queue,
                            ImmutableMap.<EngineCategoryDTO, List<EngineSubCategoryDTO>> builder()
                                    .putAll(result)
                                    .put(engineCategory, versions)
                                    .build(),
                            callback),
                    e -> Platform.runLater(() -> {
                        final ErrorDialog errorDialog = ErrorDialog.builder()
                                .withMessage(tr("Error"))
                                .withException(e)
                                .withOwner(this.enginesView.getContent().getScene().getWindow())
                                .build();

                        errorDialog.showAndWait();
                    }));
        }
    }

    private void showInstallFromUrlDialog(EngineDTO engineDTO) {
        final Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(tr("Install Wine from URL"));
        dialog.setHeaderText(tr("Install a Wine build from an external archive URL"));
        dialog.initOwner(this.enginesView.getContent().getScene().getWindow());

        final ButtonType installButtonType = new ButtonType(tr("Install"), ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(installButtonType, ButtonType.CANCEL);

        final TextField versionField = new TextField();
        versionField.setPromptText(tr("Version name (e.g. custom-8.0)"));

        final TextField urlField = new TextField();
        urlField.setPromptText(tr("Archive URL"));

        final GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label(tr("Version")), 0, 0);
        grid.add(versionField, 1, 0);
        grid.add(new Label(tr("URL")), 0, 1);
        grid.add(urlField, 1, 1);
        dialog.getDialogPane().setContent(grid);

        final Node installButton = dialog.getDialogPane().lookupButton(installButtonType);
        installButton.disableProperty()
                .bind(versionField.textProperty().isEmpty().or(urlField.textProperty().isEmpty()));

        dialog.showAndWait().filter(result -> result == installButtonType)
                .ifPresent(ignored -> installWineFromExternalUrl(engineDTO, versionField.getText().trim(),
                        urlField.getText().trim()));
    }

    private void installWineFromExternalUrl(EngineDTO engineDTO, String version, String externalUrl) {
        final SimpleConfirmDialog confirmMessage = SimpleConfirmDialog.builder()
                .withTitle(tr("Install {0}", version))
                .withMessage(tr("Are you sure you want to install {0} from {1}?", version, externalUrl))
                .withOwner(enginesView.getContent().getScene().getWindow())
                .withResizable(true)
                .withYesCallback(() -> this.enginesManager.getEngine(engineDTO.getId(), engine -> {
                    try {
                        if (!"wine".equalsIgnoreCase(engineDTO.getId())) {
                            throw new IllegalArgumentException(
                                    tr("Install from URL is only supported for Wine engines"));
                        }

                        final Path targetDirectory = Path
                                .of(engine.getLocalDirectory(engineDTO.getSubCategory(), version));
                        Files.createDirectories(targetDirectory);

                        final String archiveExtension = getArchiveExtension(externalUrl);
                        final Path tempFile = Files.createTempFile("wine-external-", archiveExtension);

                        try {
                            downloader.get(externalUrl, tempFile.toString(), progress -> {
                                // no-op: progress already handled by downloader internals
                            });
                            extractor.uncompressWithoutTracking(tempFile.toFile(), targetDirectory.toFile(),
                                    progress -> {
                                        // no-op: extraction runs in background thread
                                    });
                        } finally {
                            Files.deleteIfExists(tempFile);
                        }

                        this.versionsCache.remove(engineDTO.getId());
                        this.forceViewUpdate();
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            final ErrorDialog errorDialog = ErrorDialog.builder()
                                    .withMessage(tr("Error"))
                                    .withException(e)
                                    .withOwner(this.enginesView.getContent().getScene().getWindow())
                                    .build();

                            errorDialog.showAndWait();
                        });
                    }
                }, e -> Platform.runLater(() -> {
                    final ErrorDialog errorDialog = ErrorDialog.builder()
                            .withMessage(tr("Error"))
                            .withException(e)
                            .withOwner(this.enginesView.getContent().getScene().getWindow())
                            .build();

                    errorDialog.showAndWait();
                })))
                .build();

        confirmMessage.showAndCallback();
    }

    private String getArchiveExtension(String externalUrl) {
        try {
            final String path = new URL(externalUrl).toURI().getPath();
            if (path == null || path.isBlank()) {
                return ".tar.gz";
            }

            final String lowerPath = path.toLowerCase(Locale.ROOT);
            if (lowerPath.endsWith(".tar.gz")) {
                return ".tar.gz";
            }
            if (lowerPath.endsWith(".tar.xz")) {
                return ".tar.xz";
            }
            if (lowerPath.endsWith(".tar.bz2")) {
                return ".tar.bz2";
            }
            if (lowerPath.endsWith(".zip")) {
                return ".zip";
            }
            if (lowerPath.endsWith(".tar")) {
                return ".tar";
            }

            return ".tar.gz";
        } catch (IOException | URISyntaxException e) {
            return ".tar.gz";
        }
    }

    /**
     * Forces an update of the view
     */
    private void forceViewUpdate() {
        this.populateView(this.repositoryCache, this.enginesCache);
    }

    /**
     * Generates css for the button design associated with the given categories
     *
     * @param categoryDTOS The categories
     */
    private void setDefaultEngineIcons(List<CategoryDTO> categoryDTOS) {
        try {
            StringBuilder cssBuilder = new StringBuilder();
            for (CategoryDTO category : categoryDTOS) {
                cssBuilder.append("#").append(category.getName().toLowerCase()).append("Button{\n");
                URI categoryIcon = category.getIcon();
                if (categoryIcon == null) {
                    cssBuilder
                            .append("-fx-background-image: url('/org/phoenicis/javafx/views/common/phoenicis.png');\n");
                } else {
                    cssBuilder.append("-fx-background-image: url('").append(categoryIcon).append("');\n");
                }
                cssBuilder.append("}\n");
            }
            String css = cssBuilder.toString();
            Path temp = Files.createTempFile("defaultEngineIcons", ".css").toAbsolutePath();
            File tempFile = temp.toFile();
            tempFile.deleteOnExit();
            Files.write(temp, css.getBytes());
            String defaultEngineIconsCss = temp.toUri().toString();

            themeManager.setDefaultEngineIconsCss(defaultEngineIconsCss);
        } catch (IOException e) {
            LOGGER.warn("Could not set default engine icons.", e);
        }
    }
}