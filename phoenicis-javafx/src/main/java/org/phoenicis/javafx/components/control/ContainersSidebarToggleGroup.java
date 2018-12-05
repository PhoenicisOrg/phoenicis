package org.phoenicis.javafx.components.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.javafx.components.skin.ContainersSidebarToggleGroupSkin;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersSidebar;

import java.util.function.Consumer;

/**
 * A toggle group component used inside the {@link ContainersSidebar}
 */
public class ContainersSidebarToggleGroup extends
        SidebarToggleGroupBase<ContainerCategoryDTO, ContainersSidebarToggleGroup, ContainersSidebarToggleGroupSkin> {
    /**
     * A consumer, which is called when the "all" categories button has been selected
     */
    private final ObjectProperty<Runnable> onAllCategorySelection;

    /**
     * A consumer, which is called when a category has been selected
     */
    private final ObjectProperty<Consumer<ContainerCategoryDTO>> onCategorySelection;

    /**
     * Constructor
     *
     * @param title The title of the containers sidebar toggle group
     */
    public ContainersSidebarToggleGroup(String title) {
        super(title);

        this.onAllCategorySelection = new SimpleObjectProperty<>();
        this.onCategorySelection = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainersSidebarToggleGroupSkin createSkin() {
        return new ContainersSidebarToggleGroupSkin(this);
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

    public Consumer<ContainerCategoryDTO> getOnCategorySelection() {
        return onCategorySelection.get();
    }

    public ObjectProperty<Consumer<ContainerCategoryDTO>> onCategorySelectionProperty() {
        return onCategorySelection;
    }

    public void setOnCategorySelection(Consumer<ContainerCategoryDTO> onCategorySelection) {
        this.onCategorySelection.set(onCategorySelection);
    }
}
