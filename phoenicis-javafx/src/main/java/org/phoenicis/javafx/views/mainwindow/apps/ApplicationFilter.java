package org.phoenicis.javafx.views.mainwindow.apps;

import javafx.beans.property.*;
import javafx.collections.transformation.FilteredList;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.CategoryDTO;
import org.phoenicis.repository.dto.ScriptDTO;

import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.prefs.PreferenceChangeEvent;

/**
 * Created by marc on 29.03.17.
 */
public class ApplicationFilter {
    private final FilteredList<ApplicationDTO> filteredApplicationList;
    private final BiPredicate<String, ApplicationDTO> filterTextMatcher;

    private ObjectProperty<Predicate<ScriptDTO>> scriptFilter;

    private StringProperty filterText;
    private Optional<CategoryDTO> filterCategory;

    private BooleanProperty containFreeApplications;
    private BooleanProperty containCommercialApplications;

    private BooleanProperty containNoCDApplications;

    private BooleanProperty containTestingApplications;

    public ApplicationFilter(FilteredList<ApplicationDTO> filteredApplicationList,
            BiPredicate<String, ApplicationDTO> filterTextMatcher) {
        this.filteredApplicationList = filteredApplicationList;
        this.filterTextMatcher = filterTextMatcher;

        this.scriptFilter = new SimpleObjectProperty<>(this::filter);

        this.filterText = new SimpleStringProperty("");
        this.filterCategory = Optional.empty();

        this.containFreeApplications = new SimpleBooleanProperty();
        this.containFreeApplications
                .addListener((observableValue, oldValue, newValue) -> filteredApplicationList.setPredicate(this::filter));

        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containCommercialApplications
                .addListener((observableValue, oldValue, newValue) -> filteredApplicationList.setPredicate(this::filter));

        this.containNoCDApplications = new SimpleBooleanProperty();
        this.containNoCDApplications
                .addListener((observableValue, oldValue, newValue) -> filteredApplicationList.setPredicate(this::filter));

        this.containTestingApplications = new SimpleBooleanProperty();
        this.containTestingApplications
                .addListener((observableValue, oldValue, newValue) -> filteredApplicationList.setPredicate(this::filter));
    }

    public void setFilterText(String filterText) {
        this.filterText.setValue(filterText);
        this.filteredApplicationList.setPredicate(this::filter);
    }

    public void setFilterCategory(CategoryDTO category) {
        this.filterCategory = Optional.ofNullable(category);
        this.filteredApplicationList.setPredicate(this::filter);
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

    public ObjectProperty<Predicate<ScriptDTO>> scriptFilterProperty() {
        return this.scriptFilter;
    }

    public void clearAll() {
        this.filterText.setValue("");
        this.filterCategory = Optional.empty();

        this.filteredApplicationList.setPredicate(this::filter);
    }

    public boolean filter(ApplicationDTO application) {
        boolean result = true;

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
            result &= application.getScripts().stream().anyMatch(script -> script.getTestingOperatingSystems().isEmpty());
        }

        return result && filterTextMatcher.test(filterText.getValue(), application);
    }

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
