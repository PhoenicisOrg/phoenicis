package org.phoenicis.javafx.views.mainwindow.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.views.common.AdhocList;
import org.phoenicis.javafx.views.common.MappedList;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This class represents a group of toggle buttons in the sidebar.
 * This class must be used together with an {@link ObservableList}, containing the objects which are to be shown in this SidebarToggleGroup.
 * For every object inside the {@link ObservableList} a {@link ToggleButton} is created, which is then shown inside this SidebarToggleGroup.
 * For the creation of the {@link ToggleButton}s a converter function is used, which must be passed to the constructor of this class.
 *
 * @author marc
 * @since 28.03.17
 */
public class SidebarToggleGroup<E> extends SidebarGroup {
    /**
     * The ToggleGroup to which all ToggleButtons in this SidebarToggleGroup are added
     */
    private final ToggleGroup toggleGroup;

    /**
     * An optional {@link ToggleButton}, which represents the selection of all categories at once.
     * If this field is {@link Optional#empty()} no such button is used
     */
    private final Optional<ToggleButton> allButton;

    /**
     * A {@link VBox} containing all shown {@link ToggleButton}s belonging to this SidebarToggleGroup
     */
    private final VBox toggleButtonBox;

    /**
     * An {@link ObservableList} containing all objects for which a {@link ToggleButton} is to be shown in this SidebarToggleGroup
     */
    private final ObservableList<E> elements;

    /**
     * An {@link ObservableList} that takes all objects in <code>elements</code> and converts them to a {@link ToggleButton}
     */
    private final MappedList<? extends ToggleButton, E> mappedToggleButtons;

    /**
     * An {@link ObservableList} containing both the <code>allButton</code>, if it's available, and the {@link ToggleButton}s
     * inside <code>mappedToggleButtons</code>
     */
    private final AdhocList<? extends ToggleButton> adhocToggleButtons;

    /**
     * Constructor
     *
     * @param name              The title of this SidebarToggleGroup
     * @param allButtonSupplier A supplier function used to create the optional "all" ToggleButton. If no such button is needed null can be used
     * @param converter         A converter function used to convert the source objects to ToggleButtons
     */
    private SidebarToggleGroup(String name, Supplier<ToggleButton> allButtonSupplier,
            Function<E, ? extends ToggleButton> converter) {
        super(name);

        this.toggleGroup = new ToggleGroup();
        this.toggleButtonBox = new VBox();
        this.toggleButtonBox.getStyleClass().add("sidebarInside");

        this.elements = FXCollections.observableArrayList();
        this.mappedToggleButtons = new MappedList<ToggleButton, E>(this.elements, value -> converter.apply(value));

        if (allButtonSupplier != null) {
            ToggleButton newAllButton = allButtonSupplier.get();

            this.allButton = Optional.of(newAllButton);
            this.adhocToggleButtons = new AdhocList<ToggleButton>(this.mappedToggleButtons, newAllButton);
        } else {
            this.allButton = Optional.empty();
            this.adhocToggleButtons = new AdhocList<ToggleButton>(this.mappedToggleButtons);
        }

        Bindings.bindContent(this.toggleGroup.getToggles(), this.adhocToggleButtons);
        Bindings.bindContent(this.toggleButtonBox.getChildren(), this.adhocToggleButtons);

        /*
         * Ensure that one ToggleButton is always selected.
         */
        this.adhocToggleButtons.addListener(new ListChangeListener<ToggleButton>() {
            @Override
            public void onChanged(Change<? extends ToggleButton> change) {
                // the adhoc toggle buttons contain no selected button anymore
                if (!adhocToggleButtons.stream().anyMatch(ToggleButton::isSelected)) {
                    if (adhocToggleButtons.size() > 0) {
                        // to solve this automatically select the first button
                        adhocToggleButtons.get(0).fire();
                    }
                }
            }
        });

        this.getChildren().add(this.toggleButtonBox);
    }

    /**
     * Constructor
     *
     * @param name      The title of this SidebarToggleGroup
     * @param converter A converter function used to convert the source objects to ToggleButtons
     */
    private SidebarToggleGroup(String name, Function<E, ? extends ToggleButton> converter) {
        this(name, null, converter);
    }

    /**
     * This method creates a new SidebarToggleGroup without an "all" categories ToggleButton using the given arguments
     *
     * @param name      The title of the new SidebarToggleGroup
     * @param converter The converter function used by the new SidebarToggleGroup to convert the source objects to ToggleButtons
     * @param <T>       The type of the source objects in the new SidebarToggleGroup
     * @return The newly created SidebarToggleGroup with the given arguments
     */
    public static <T> SidebarToggleGroup<T> create(String name, Function<T, ? extends ToggleButton> converter) {
        return new SidebarToggleGroup<T>(name, converter);
    }

    /**
     * This method creates a new SidebarToggleGroup using the given arguments
     *
     * @param name              The title of the new SidebarToggleGroup
     * @param allButtonSupplier The suppliert function used to create the "all" categories ToggleButton
     * @param converter         The converter function used by the new SidebarToggleGroup to convert the source objects to ToggleButtons
     * @param <T>               The type of the source objects in the new SidebarToggleGroup
     * @return The newly created SidebarToggleGroup with the given arguments
     */
    public static <T> SidebarToggleGroup<T> create(String name, Supplier<ToggleButton> allButtonSupplier,
            Function<T, ? extends ToggleButton> converter) {
        return new SidebarToggleGroup<T>(name, allButtonSupplier, converter);
    }

    /**
     * This method returns the {@link ObservableList} containing the source objects used to create the shown {@link ToggleButton}s.
     * This {@link ObservableList} can be used to bind another {@link ObservableList} to this SidebarToggleGroup
     *
     * @return The used {@link ObservableList}
     */
    public ObservableList<E> getElements() {
        return this.elements;
    }

    /**
     * This method selects the "all" categories {@link ToggleButton} inside this SidebarToggleGroup.
     * If no such button exists this method does nothing
     */
    public void selectAll() {
        this.allButton.ifPresent(button -> button.fire());
    }

    /**
     * This method selects the ToggleButton at a given position inside this SidebarToggleGroup.
     * For the position an optional "all" categories {@link ToggleButton} is ignored
     *
     * @param elementIndex The position of the to be selected {@link ToggleButton}
     * @throws IllegalArgumentException This exception is thrown, if the given position <code>elementIndex</code>
     *                                  is outside the range of the elements contained in this SidebarToggleGroup
     */
    public void select(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= this.elements.size()) {
            throw new IllegalArgumentException(String.format("Element at index %d doesn't exist", elementIndex));
        } else {
            mappedToggleButtons.get(elementIndex).fire();
        }
    }

    /**
     * This method selects the ToggleButton belonging to a given element contained in the <code>elements</code> {@link ObservableList}
     * inside this SidebarToggleGroup.
     *
     * @param element The element, whose corresponding {@link javafx.scene.control.ToggleButton} is to be selected
     * @throws IllegalArgumentException This exception is thrown, if the given <code>element</code> doesn't exist in this SidebarToggleGroup
     */
    public void select(E element) {
        this.select(this.elements.indexOf(element));
    }
}
