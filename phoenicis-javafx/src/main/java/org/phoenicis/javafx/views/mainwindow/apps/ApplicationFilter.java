package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.property.*;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * A filter implementation for the "Apps" tab.
 *
 * @author marc
 * @since 29.03.17
 */
public class ApplicationFilter {
    private final BiPredicate<String, ApplicationDTO> filterTextMatcher;

    private final ObjectProperty<Predicate<ApplicationDTO>> applicationFilter;
    private final ObjectProperty<Predicate<ScriptDTO>> scriptFilter;

    private StringProperty filterText;
    private Optional<CategoryDTO> filterCategory;

    private BooleanProperty containFreeApplications;
    private BooleanProperty containCommercialApplications;

    private BooleanProperty containNoCDApplications;

    private BooleanProperty containTestingApplications;

    /**
     * Constructor
     *
     * @param filterTextMatcher The matcher function for the filter text
     */
    public ApplicationFilter(BiPredicate<String, ApplicationDTO> filterTextMatcher) {
        this.filterTextMatcher = filterTextMatcher;

        this.applicationFilter = new SimpleObjectProperty<>(this::filter);
        this.scriptFilter = new SimpleObjectProperty<>(this::filter);

        this.filterText = new SimpleStringProperty("");
        this.filterCategory = Optional.empty();

        this.containFreeApplications = new SimpleBooleanProperty();
        this.containFreeApplications.addListener((observableValue, oldValue, newValue) -> this.fire());

        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containCommercialApplications.addListener((observableValue, oldValue, newValue) -> this.fire());

        this.containNoCDApplications = new SimpleBooleanProperty();
        this.containNoCDApplications.addListener((observableValue, oldValue, newValue) -> this.fire());

        this.containTestingApplications = new SimpleBooleanProperty();
        this.containTestingApplications.addListener((observableValue, oldValue, newValue) -> this.fire());
    }

    /**
     * Triggers a filter update
     */
    public void fire() {
        applicationFilter.setValue(this::filter);
        scriptFilter.setValue(this::filter);
    }

    /**
     * Sets the filter text inside the filter and triggers a filter update
     *
     * @param filterText The new entered filter text
     */
    public void setFilterText(String filterText) {
        this.filterText.setValue(filterText);

        this.fire();
    }

    /**
     * Sets the selected category inside the filter and triggers a filter update
     *
     * @param category The new selected category, or null if no/all category has been selected
     */
    public void setFilterCategory(CategoryDTO category) {
        this.filterCategory = Optional.ofNullable(category);

        this.fire();
    }

    public BooleanProperty containFreeApplicationsProperty() {
        return this.containFreeApplications;
    }

    public BooleanProperty containCommercialApplicationsProperty() {
        return this.containCommercialApplications;
    }

    public BooleanProperty containNoCDApplicationsProperty() {
        return this.containNoCDApplications;
    }

    public BooleanProperty containTestingApplicationsProperty() {
        return this.containTestingApplications;
    }

    public ObjectProperty<Predicate<ApplicationDTO>> applicationFilterProperty() {
        return this.applicationFilter;
    }

    public ObjectProperty<Predicate<ScriptDTO>> scriptFilterProperty() {
        return this.scriptFilter;
    }

    /**
     * Clears the <code>filterText</code> and the <code>filterCategory</code>.
     * Afterwards a filter update gets triggered
     */
    public void clearAll() {
        this.filterText.setValue("");
        this.filterCategory = Optional.empty();

        this.fire();
    }

    /**
     * Filter function for {@link ApplicationDTO} objects
     *
     * @param application The application which should checked
     * @return True if the given <code>application</code> fulfills the filter conditions, false otherwise
     */
    public boolean filter(ApplicationDTO application) {
        boolean result = true;

        /*
         * If a category has been selected by the user, show only applications that belong to the selected category
         */
        if (filterCategory.isPresent()) {
            result &= filterCategory.get().getApplications().contains(application);
        }

        /*
         * If "free" is not selected don't show free games
         */
        if (!containFreeApplications.getValue()) {
            result &= application.getScripts().stream().anyMatch(script -> !script.isFree());
        }

        /*
         * If "commercial" is not selected don't show commercial games
         */
        if (!containCommercialApplications.getValue()) {
            result &= application.getScripts().stream().anyMatch(script -> script.isFree());
        }

        /*
         * If "No CD required" is selected show't show games that require a CD
         */
        if (containNoCDApplications.getValue()) {
            result &= application.getScripts().stream().anyMatch(script -> !script.isRequiresNoCD());
        }

        /*
         * If "Testing" is not selected don't show games that are currently in a testing stage
         */
        if (!containTestingApplications.getValue()) {
            result &= application.getScripts().stream()
                    .anyMatch(script -> script.getTestingOperatingSystems().isEmpty());
        }

        return result && filterTextMatcher.test(filterText.getValue(), application);
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
         * If "free" is not selected don't show free games
         */
        if (!containFreeApplications.getValue()) {
            result &= !script.isFree();
        }

        /*
         * If "commercial" is not selected don't show commercial games
         */
        if (!containCommercialApplications.getValue()) {
            result &= script.isFree();
        }

        /*
         * If "No CD required" is selected show't show games that require a CD
         */
        if (containNoCDApplications.getValue()) {
            result &= !script.isRequiresNoCD();
        }

        /*
         * If "Testing" is not selected don't show games that are currently in a testing stage
         */
        if (!containTestingApplications.getValue()) {
            result &= script.getTestingOperatingSystems().isEmpty();
        }

        return result;
    }

}
