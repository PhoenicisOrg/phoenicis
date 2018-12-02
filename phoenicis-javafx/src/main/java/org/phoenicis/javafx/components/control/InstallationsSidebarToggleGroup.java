package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.skin.InstallationsSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.installations.InstallationsSidebar;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationCategoryDTO;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link InstallationsSidebar}
 */
public class InstallationsSidebarToggleGroup extends
        SidebarToggleGroupBase<InstallationCategoryDTO, InstallationsSidebarToggleGroup, InstallationsSidebarToggleGroupSkin> {
    /**
     * A consumer, which is called when the "all" categories button has been selected
     */
    private final ObjectProperty<Runnable> onAllCategorySelection;

    /**
     * A consumer, which is called when a category has been selected
     */
    private final ObjectProperty<Consumer<InstallationCategoryDTO>> onCategorySelection;

    /**
     * Constructor
     *
     * @param title The title of the installations sidebar toggle group
     */
    public InstallationsSidebarToggleGroup(String title) {
        super(title);

        this.onAllCategorySelection = new SimpleObjectProperty<>();
        this.onCategorySelection = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstallationsSidebarToggleGroupSkin createSkin() {
        return new InstallationsSidebarToggleGroupSkin(this);
    }

    public Runnable getOnAllCategorySelection() {
        return onAllCategorySelection.get();
    }

    public ObjectProperty<Runnable> onAllCategorySelectionProperty() {
        return onAllCategorySelection;
    }

    public void setOnAllCategorySelection(Runnable onAllCategorySelection) {
        this.onAllCategorySelection.set(onAllCategorySelection);
    }

    public Consumer<InstallationCategoryDTO> getOnCategorySelection() {
        return onCategorySelection.get();
    }

    public ObjectProperty<Consumer<InstallationCategoryDTO>> onCategorySelectionProperty() {
        return onCategorySelection;
    }

    public void setOnCategorySelection(Consumer<InstallationCategoryDTO> onCategorySelection) {
        this.onCategorySelection.set(onCategorySelection);
    }
}
