package org.phoenicis.javafx.components.application.control;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.phoenicis.entities.OperatingSystem;
import org.phoenicis.javafx.components.application.skin.ApplicationSidebarSkin;
import org.phoenicis.javafx.components.application.utils.CategoryFilter;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.repository.dto.CategoryDTO;

/**
 * A sidebar implementation for the applications tab
 */
public class ApplicationSidebar extends ExtendedSidebarBase<CategoryDTO, ApplicationSidebar, ApplicationSidebarSkin>
        implements CategoryFilter {
    /**
     * The fuzzy search ratio
     */
    private final DoubleProperty fuzzySearchRatio;

    /**
     * The operating system
     */
    private final ObjectProperty<OperatingSystem> operatingSystem;

    /**
     * Information about whether the user wants to see commercial applications or not
     */
    private final BooleanProperty containCommercialApplications;

    /**
     * Information about whether the user wants to see scripts requiring patches
     */
    private final BooleanProperty containRequiresPatchApplications;

    /**
     * Information about whether the user wants to see scripts that are still in testing
     */
    private final BooleanProperty containTestingApplications;

    /**
     * Information about whether the user wants to see scripts that are not tested on his operating system
     */
    private final BooleanProperty containAllOSCompatibleApplications;

    /**
     * Constructor
     *
     * @param items The items shown inside a toggle button group in the sidebar
     */
    public ApplicationSidebar(ObservableList<CategoryDTO> items) {
        super(items);

        this.fuzzySearchRatio = new SimpleDoubleProperty();
        this.operatingSystem = new SimpleObjectProperty<>();
        this.containCommercialApplications = new SimpleBooleanProperty();
        this.containRequiresPatchApplications = new SimpleBooleanProperty();
        this.containTestingApplications = new SimpleBooleanProperty();
        this.containAllOSCompatibleApplications = new SimpleBooleanProperty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplicationSidebarSkin createSkin() {
        return new ApplicationSidebarSkin(this);
    }

    public double getFuzzySearchRatio() {
        return fuzzySearchRatio.get();
    }

    @Override
    public DoubleProperty fuzzySearchRatioProperty() {
        return fuzzySearchRatio;
    }

    public void setFuzzySearchRatio(double fuzzySearchRatio) {
        this.fuzzySearchRatio.set(fuzzySearchRatio);
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem.get();
    }

    @Override
    public ObjectProperty<OperatingSystem> operatingSystemProperty() {
        return operatingSystem;
    }

    public void setOperatingSystem(OperatingSystem operatingSystem) {
        this.operatingSystem.set(operatingSystem);
    }

    public boolean isContainCommercialApplications() {
        return containCommercialApplications.get();
    }

    @Override
    public BooleanProperty containCommercialApplicationsProperty() {
        return containCommercialApplications;
    }

    public void setContainCommercialApplications(boolean containCommercialApplications) {
        this.containCommercialApplications.set(containCommercialApplications);
    }

    public boolean isContainRequiresPatchApplications() {
        return containRequiresPatchApplications.get();
    }

    @Override
    public BooleanProperty containRequiresPatchApplicationsProperty() {
        return containRequiresPatchApplications;
    }

    public void setContainRequiresPatchApplications(boolean containRequiresPatchApplications) {
        this.containRequiresPatchApplications.set(containRequiresPatchApplications);
    }

    public boolean isContainTestingApplications() {
        return containTestingApplications.get();
    }

    @Override
    public BooleanProperty containTestingApplicationsProperty() {
        return containTestingApplications;
    }

    public void setContainTestingApplications(boolean containTestingApplications) {
        this.containTestingApplications.set(containTestingApplications);
    }

    public boolean isContainAllOSCompatibleApplications() {
        return containAllOSCompatibleApplications.get();
    }

    @Override
    public BooleanProperty containAllOSCompatibleApplicationsProperty() {
        return containAllOSCompatibleApplications;
    }

    public void setContainAllOSCompatibleApplications(boolean containAllOSCompatibleApplications) {
        this.containAllOSCompatibleApplications.set(containAllOSCompatibleApplications);
    }
}
