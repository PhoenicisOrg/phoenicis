package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.property.*;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.apache.commons.lang.StringUtils;
import org.phoenicis.javafx.views.AbstractFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

/**
 * A filter implementation for the "Apps" tab.
 *
 * @author marc
 * @since 29.03.17
 */
public class ApplicationFilter extends AbstractFilter {
    private final OperatingSystemFetcher operatingSystemFetcher;

    private final double fuzzySearchRatio;

    private final StringProperty filterText;

    private final ObjectProperty<CategoryDTO> filterCategory;

    private final BooleanProperty containCommercialApplications;

    private final BooleanProperty containRequiresPatchApplications;

    private final BooleanProperty containTestingApplications;

    private final BooleanProperty containAllOSCompatibleApplications;

    public ApplicationFilter(OperatingSystemFetcher operatingSystemFetcher, double fuzzySearchRatio) {
        super();

        this.operatingSystemFetcher = operatingSystemFetcher;
        this.fuzzySearchRatio = fuzzySearchRatio;

        this.filterText = new SimpleStringProperty();
        this.filterCategory = new SimpleObjectProperty<>();
        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containRequiresPatchApplications = new SimpleBooleanProperty();
        this.containTestingApplications = new SimpleBooleanProperty();
        this.containAllOSCompatibleApplications = new SimpleBooleanProperty();
    }

    /**
     * Filter function for {@link CategoryDTO} objects
     *
     * @param category The category which should be checked
     * @return True if the given <code>category</code> fulfills the filter condition, false otherwise
     */
    public boolean filter(CategoryDTO category) {
        /*
         * A category can be shown, if it contains at least one visible application
         */
        return category.getApplications().stream().anyMatch(
                application -> filter(application, true));
    }

    /**
     * Filter function for {@link ApplicationDTO} objects.
     * This method call {@link #filter(ApplicationDTO, boolean)} with <code>ignoreFilterCategoryTest = false</code>.
     *
     * @param application The application which should checked
     * @return True if the given <code>application</code> fulfills the filter conditions, false otherwise
     */
    public boolean filter(ApplicationDTO application) {
        return filter(application, false);
    }

    /**
     * Filter function for {@link ApplicationDTO} objects
     *
     * @param application The application which should checked
     * @param ignoreFilterCategoryTest True if an optional filter category should be ignored, false otherwise
     * @return True if the given <code>application</code> fulfills the filter conditions, false otherwise
     */
    private boolean filter(ApplicationDTO application, boolean ignoreFilterCategoryTest) {
        final boolean matchesFilterCategory = ignoreFilterCategoryTest || Optional.ofNullable(filterCategory.getValue())
                .map(category -> category.getApplications().contains(application)).orElse(true);

        final boolean matchesAtLeastOneScript = application.getScripts().stream().anyMatch(this::filter);

        final boolean matchesApplicationName = Optional.ofNullable(filterText.getValue())
                .map(filterText -> StringUtils.isEmpty(filterText) || FuzzySearch
                        .partialRatio(application.getName().toLowerCase(), filterText) > fuzzySearchRatio)
                .orElse(true);

        /*
         * An application can be shown, if:
         * - it belongs to the filter category, if such a category is set
         * - it contains at least one visible script
         * - its text matches the filter text
         */
        return matchesFilterCategory && matchesApplicationName && matchesAtLeastOneScript;
    }

    /**
     * Filter function for {@link ScriptDTO} objects
     *
     * @param script The script which should be checked
     * @return True if the given <code>script</code> fulfills the filter conditions, false otherwise
     */
    public boolean filter(ScriptDTO script) {
        boolean result = true;

        /*
         * If "commercial" is not selected, don't show commercial games
         */
        if (!isContainCommercialApplications()) {
            result &= script.isFree();
        }

        /*
         * If "Requires patch" is selected, show show games that require a patch to run (e.g. no CD)
         */
        if (isContainRequiresPatchApplications()) {
            result &= !script.isRequiresPatch();
        }

        /*
         * If "Testing" is not selected, don't show games that are currently in a testing stage
         */
        if (!isContainTestingApplications()) {
            result &= CollectionUtils.isEmpty(script.getTestingOperatingSystems());
        }

        /*
         * If "All Operating Systems" is not selected, show only applications that fit to the used operating system
         */
        if (!isContainAllOSCompatibleApplications()) {
            result &= Optional.ofNullable(script.getCompatibleOperatingSystems())
                    .map(compatibleOperatingSystems -> compatibleOperatingSystems
                            .contains(operatingSystemFetcher.fetchCurrentOperationSystem()))
                    .orElse(false);
        }

        return result;
    }

    public String getFilterText() {
        return filterText.get();
    }

    public StringProperty filterTextProperty() {
        return filterText;
    }

    public CategoryDTO getFilterCategory() {
        return filterCategory.get();
    }

    public ObjectProperty<CategoryDTO> filterCategoryProperty() {
        return filterCategory;
    }

    public boolean isContainCommercialApplications() {
        return containCommercialApplications.get();
    }

    public BooleanProperty containCommercialApplicationsProperty() {
        return containCommercialApplications;
    }

    public boolean isContainRequiresPatchApplications() {
        return containRequiresPatchApplications.get();
    }

    public BooleanProperty containRequiresPatchApplicationsProperty() {
        return containRequiresPatchApplications;
    }

    public boolean isContainTestingApplications() {
        return containTestingApplications.get();
    }

    public BooleanProperty containTestingApplicationsProperty() {
        return containTestingApplications;
    }

    public boolean isContainAllOSCompatibleApplications() {
        return containAllOSCompatibleApplications.get();
    }

    public BooleanProperty containAllOSCompatibleApplicationsProperty() {
        return containAllOSCompatibleApplications;
    }
}
