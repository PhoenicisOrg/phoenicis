package org.phoenicis.javafx.components.common.skin;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetSelector;

/**
 * The base skin for all {@link ExtendedSidebarBase} implementations
 *
 * @param <E> The element type of the toggle button group
 * @param <C> The concrete component type
 * @param <S> The concrete skin type
 */
public abstract class ExtendedSidebarSkinBase<E, C extends ExtendedSidebarBase<E, C, S>, S extends ExtendedSidebarSkinBase<E, C, S>>
        extends SidebarSkinBase<E, C, S> {
    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    protected ExtendedSidebarSkinBase(C control) {
        super(control);
    }

    /**
     * Creates a new {@link CheckBox} with containing the given {@link String text}
     *
     * @param text The text
     * @return The new {@link CheckBox}
     */
    protected static CheckBox createCheckBox(String text) {
        final CheckBox checkBox = new CheckBox(text);

        checkBox.getStyleClass().add("sidebarCheckBox");

        return checkBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        final BorderPane container = new BorderPane();
        container.getStyleClass().add("sidebar");

        // set the search box at the top of the side bar
        container.setTop(createSearchBox());
        // set the main content of the sidebar
        container.setCenter(createMainContent());
        // set the list widget selector at the bottom of the sidebar
        container.setBottom(createListWidgetSelector());

        getChildren().addAll(container);
    }

    /**
     * Creates the {@link SearchBox} shown inside the sidebar
     *
     * @return The {@link SearchBox} shown inside the sidebar
     */
    private SearchBox createSearchBox() {
        return new SearchBox(getControl().searchTermProperty());
    }

    /**
     * Creates the {@link ListWidgetSelector} shown inside the sidebar
     *
     * @return The {@link ListWidgetSelector} shown inside the sidebar
     */
    private ListWidgetSelector createListWidgetSelector() {
        return new ListWidgetSelector(getControl().selectedListWidgetProperty());
    }
}
