package org.phoenicis.javafx.views.mainwindow.containers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.phoenicis.containers.dto.ContainerCategoryDTO;
import org.phoenicis.containers.dto.ContainerDTO;

import java.util.Optional;

/**
 * Filter class for the "Containers" tab
 *
 * @author Marc Arndt
 */
public class ContainersFilter {
    /**
     * The entered search term.
     * If no search term has been entered, this value is {@link Optional#empty()}.
     */
    private StringProperty searchTerm;

    /**
     * The selected container category.
     * If no container category has been selected, this value is {@link Optional#empty()}.
     */
    private ObjectProperty<ContainerCategoryDTO> selectedContainerCategory;

    /**
     * Constructor
     * Assumes an empty search term and no selected container category
     */
    public ContainersFilter() {
        super();

        this.searchTerm = new SimpleStringProperty();
        this.selectedContainerCategory = new SimpleObjectProperty<>();
    }

    /**
     * Filters a given container category
     *
     * @param containerCategory The to be filtered container category
     * @return True if the container category should be shown, false otherwise
     */
    public boolean filter(ContainerCategoryDTO containerCategory) {
        return Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> containerCategory.getContainers().stream().anyMatch(this::filter))
                .orElse(true);
    }

    /**
     * Filters a given container
     *
     * @param installation The to be filtered container
     * @return True if the container should be shown, false otherwise
     */
    public boolean filter(ContainerDTO installation) {
        final boolean searchTermConstraint = Optional.ofNullable(searchTerm.getValueSafe())
                .map(searchTerm -> installation.getName().toLowerCase().contains(searchTerm.toLowerCase()))
                .orElse(true);

        final boolean selectedContainerCategoryConstraint = Optional.ofNullable(selectedContainerCategory.getValue())
                .map(
                        selectedContainerCategory -> selectedContainerCategory.getContainers().contains(installation))
                .orElse(true);

        return searchTermConstraint && selectedContainerCategoryConstraint;
    }

    public StringProperty searchTermProperty() {
        return searchTerm;
    }

    public ObjectProperty<ContainerCategoryDTO> selectedContainerCategoryProperty() {
        return selectedContainerCategory;
    }
}
