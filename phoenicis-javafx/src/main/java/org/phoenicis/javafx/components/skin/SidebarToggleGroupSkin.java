package org.phoenicis.javafx.components.skin;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.behavior.SidebarToggleGroupBehavior;
import org.phoenicis.javafx.components.control.SidebarToggleGroup;
import org.phoenicis.javafx.views.common.lists.AdhocList;
import org.phoenicis.javafx.views.common.lists.MappedList;

import java.util.Optional;

public abstract class SidebarToggleGroupSkin<E>
        extends BehaviorSkinBase<SidebarToggleGroup<E>, SidebarToggleGroupSkin<E>, SidebarToggleGroupBehavior<E>> {
    /**
     * An {@link ObservableList} containing both the <code>allButton</code>, if it's available, and the
     * {@link ToggleButton}s inside <code>mappedToggleButtons</code>
     */
    private ObservableList<ToggleButton> adhocToggleButtons;

    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public SidebarToggleGroupSkin(SidebarToggleGroup<E> control) {
        super(control);
    }

    @Override
    public void initialise() {
        ToggleGroup toggleGroup = new ToggleGroup();

        ToggleButton allToggleButton = createAllButton().orElse(null);

        ObservableList<ToggleButton> mappedToggleButtons = new MappedList<>(getControl().getElements(),
                this::convertToToggleButton);

        if (allToggleButton != null) {
            adhocToggleButtons = new AdhocList<>(mappedToggleButtons, allToggleButton);
        } else {
            adhocToggleButtons = new AdhocList<>(mappedToggleButtons);
        }

        Bindings.bindContent(toggleGroup.getToggles(), this.adhocToggleButtons);

        getControl().selectedProperty().addListener(invalidated -> {
            SidebarToggleGroup.SidebarToggleButtonSelection selected = getControl().getSelected();

            if (selected.isAllButton()) {
                int elementIndex = selected.getIndex();

                adhocToggleButtons.get(elementIndex).fire();
            }
        });

        VBox container = new VBox();
        container.getStyleClass().add("sidebarInside");

        Bindings.bindContent(container.getChildren(),
                new AdhocList<>(adhocToggleButtons, createTitleLabel()));

        getChildren().addAll(container);
    }

    abstract ToggleButton convertToToggleButton(E element);

    abstract Optional<ToggleButton> createAllButton();

    private Label createTitleLabel() {
        Label title = new Label();
        title.getStyleClass().add("sidebarTitle");

        title.textProperty().bind(getControl().titleProperty());
        // only make the title label visible if the property has been set
        title.visibleProperty().bind(Bindings.isNotNull(getControl().titleProperty()));

        return title;
    }

    public ObservableList<ToggleButton> getAdhocToggleButtons() {
        return adhocToggleButtons;
    }
}
