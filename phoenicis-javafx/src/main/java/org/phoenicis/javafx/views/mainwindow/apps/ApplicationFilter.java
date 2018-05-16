package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.property.*;
import org.phoenicis.javafx.views.AbstractFilter;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;
import org.phoenicis.tools.system.OperatingSystemFetcher;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * A filter implementation for the "Apps" tab.
 *
 * @author marc
 * @since 29.03.17
 */
public class ApplicationFilter extends AbstractFilter {
    private final OperatingSystemFetcher operatingSystemFetcher;

    private final BiPredicate<String, ApplicationDTO> filterTextMatcher;

    private Optional<String> filterText;
    private Optional<CategoryDTO> filterCategory;

    private BooleanProperty containCommercialApplications;

    private BooleanProperty containRequiresPatchApplications;

    private BooleanProperty containTestingApplications;

    private BooleanProperty containAllOSCompatibleApplications;

    /**
     * Constructor
     *
     * @param filterTextMatcher The matcher function for the filter text
     */
    public ApplicationFilter(OperatingSystemFetcher operatingSystemFetcher,
            BiPredicate<String, ApplicationDTO> filterTextMatcher) {
        super();

        this.operatingSystemFetcher = operatingSystemFetcher;
        this.filterTextMatcher = filterTextMatcher;

        this.filterText = Optional.empty();
        this.filterCategory = Optional.empty();

        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containCommercialApplications
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());

        this.containRequiresPatchApplications = new SimpleBooleanProperty();
        this.containRequiresPatchApplications
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());

        this.containTestingApplications = new SimpleBooleanProperty();
        this.containTestingApplications
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());

        this.containAllOSCompatibleApplications = new SimpleBooleanProperty();
        this.containAllOSCompatibleApplications
                .addListener((observableValue, oldValue, newValue) -> this.triggerFilterChanged());
    }

    /**
     * Sets the filter text inside the filter and triggers a filter update
     *
     * @param filterText The new entered filter text
     */
    public void setFilterText(String filterText) {
        this.filterText = Optional.ofNullable(filterText);

        this.triggerFilterChanged();
    }

    /**
     * Sets the selected category inside the filter and triggers a filter update
     *
     * @param category The new selected category, or null if no/all category has been selected
     */
    public void setFilterCategory(CategoryDTO category) {
        this.filterCategory = Optional.ofNullable(category);

        this.triggerFilterChanged();
    }

    public BooleanProperty containCommercialApplicationsProperty() {
        return this.containCommercialApplications;
    }

    public BooleanProperty containRequiresPatchApplicationsProperty() {
        return this.containRequiresPatchApplications;
    }

    public BooleanProperty containTestingApplicationsProperty() {
        return this.containTestingApplications;
    }

    public BooleanProperty containAllOSCompatibleApplications() {
        return this.containAllOSCompatibleApplications;
    }

    /**
     * Clears the <code>filterText</code> and the <code>filterCategory</code>.
     * Afterwards a filter update gets triggered
     */
    public void clearAll() {
        this.filterText = Optional.empty();
        this.filterCategory = Optional.empty();

        this.triggerFilterChanged();
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
        return category.getApplications().stream().anyMatch(application -> filter(application, true));
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
        /*
         * An application can be shown, if:
         * - it belongs to the filter category, if such a category is set
         * - it contains at least one visible script
         * - its text matches the filter text
         */
        return (ignoreFilterCategoryTest
                || filterCategory.map(category -> category.getApplications().contains(application)).orElse(true))
                && application.getScripts().stream().anyMatch(script -> filter(script))
                && filterText.map(filterText -> filterTextMatcher.test(filterText, application)).orElse(true);
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
        if (!containCommercialApplications.getValue()) {
            result &= script.isFree();
        }

        /*
         * If "Requires patch" is selected, show show games that require a patch to run (e.g. no CD)
         */
        if (containRequiresPatchApplications.getValue()) {
            result &= !script.isRequiresPatch();
        }

        /*
         * If "Testing" is not selected, don't show games that are currently in a testing stage
         */
        if (!containTestingApplications.getValue()) {
            result &= CollectionUtils.isEmpty(script.getTestingOperatingSystems());
        }

        /*
         * If "All Operating Systems" is not selected, show only applications that fit to the used operating system
         */
        if (!containAllOSCompatibleApplications.getValue()) {
            result &= Optional.ofNullable(script.getCompatibleOperatingSystems())
                    .map(compatibleOperatingSystems -> compatibleOperatingSystems
                            .contains(operatingSystemFetcher.fetchCurrentOperationSystem()))
                    .orElse(false);
        }

        return result;
    }

}
