package org.phoenicis.javafx.components.engine.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.phoenicis.engines.dto.EngineCategoryDTO;
import org.phoenicis.engines.dto.EngineDTO;
import org.phoenicis.engines.dto.EngineSubCategoryDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.javafx.collections.MappedList;
import org.phoenicis.javafx.components.common.skin.SkinBase;
import org.phoenicis.javafx.components.common.widgets.control.CombinedListWidget;
import org.phoenicis.javafx.components.common.widgets.utils.ListWidgetElement;
import org.phoenicis.javafx.components.engine.control.EngineSubCategoryPanel;
import org.phoenicis.javafx.views.mainwindow.engines.EnginesFilter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A skin implementation for the {@link EngineSubCategoryPanel} component
 */
public class EngineSubCategoryPanelSkin extends SkinBase<EngineSubCategoryPanel, EngineSubCategoryPanelSkin> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public EngineSubCategoryPanelSkin(EngineSubCategoryPanel control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        // apply the filter to the engine versions
        final ObservableList<EngineVersionDTO> filteredEngineVersions = createFilteredEngineVersions();

        final CombinedListWidget<EngineVersionDTO> engineVersionsView = createListWidget(filteredEngineVersions);

        getChildren().setAll(engineVersionsView);
    }

    /**
     * Creates a {@link FilteredList} object of the engine versions by applying the {@link EnginesFilter} known to the
     * control
     *
     * @return A filtered list of the engine versions
     */
    private ObservableList<EngineVersionDTO> createFilteredEngineVersions() {
        final EnginesFilter filter = getControl().getFilter();

        final EngineCategoryDTO engineCategory = getControl().getEngineCategory();
        final EngineSubCategoryDTO engineSubCategory = getControl().getEngineSubCategory();

        final FilteredList<EngineVersionDTO> filteredEngineVersions = FXCollections
                .observableArrayList(engineSubCategory.getPackages())
                .sorted(EngineSubCategoryDTO.comparator().reversed())
                .filtered(filter.createFilter(engineCategory, engineSubCategory));

        filteredEngineVersions.predicateProperty().bind(
                Bindings.createObjectBinding(() -> filter.createFilter(engineCategory, engineSubCategory),
                        filter.searchTermProperty(),
                        filter.selectedEngineCategoryProperty(),
                        filter.showInstalledProperty(),
                        filter.showNotInstalledProperty()));

        return filteredEngineVersions;
    }

    /**
     * Creates a new {@link CombinedListWidget} object containing all {@link EngineVersionDTO} objects in the given
     * {@link ObservableList filteredEngineVersions}
     *
     * @param filteredEngineVersions An {@link ObservableList} containing all to be shown {@link EngineVersionDTO}
     *            objects
     * @return A new {@link CombinedListWidget} object containing all {@link EngineVersionDTO} objects in the given
     *         {@link ObservableList filteredEngineVersions}
     */
    private CombinedListWidget<EngineVersionDTO> createListWidget(
            ObservableList<EngineVersionDTO> filteredEngineVersions) {
        final ObservableList<ListWidgetElement<EngineVersionDTO>> listWidgetEntries = new MappedList<>(
                filteredEngineVersions,
                engineVersion -> ListWidgetElement.create(engineVersion, Files.exists(Paths.get(
                        getControl().getEnginesPath(), getControl().getEngineCategory().getName().toLowerCase(),
                        getControl().getEngineSubCategory().getName(), engineVersion.getVersion()))));

        final CombinedListWidget<EngineVersionDTO> listWidget = new CombinedListWidget<>(listWidgetEntries);

        listWidget.selectedListWidgetProperty().bind(getControl().selectedListWidgetProperty());

        listWidget.selectedElementProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final EngineVersionDTO engineItem = newValue.getItem();

                Map<String, String> userData = new HashMap<>();
                userData.put("Mono", engineItem.getMonoFile());
                userData.put("Gecko", engineItem.getGeckoFile());

                EngineDTO engineDTO = new EngineDTO.Builder()
                        .withCategory(getControl().getEngineCategory().getName())
                        .withSubCategory(getControl().getEngineSubCategory().getName())
                        .withVersion(engineItem.getVersion())
                        .withUserData(userData).build();

                Optional.ofNullable(getControl().getOnEngineSelect()).ifPresent(
                        onEngineSelect -> onEngineSelect.accept(engineDTO, getControl().getEngine()));
            }
        });

        return listWidget;
    }
}
