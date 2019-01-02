package org.phoenicis.javafx.components.common.skin;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import org.phoenicis.javafx.components.common.control.ExtendedSidebarBase;
import org.phoenicis.javafx.components.common.control.SearchBox;
import org.phoenicis.javafx.components.common.widgets.control.ListWidgetSelector;

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

    protected static CheckBox createCheckBox(String text) {
        final CheckBox checkBox = new CheckBox(text);

        checkBox.getStyleClass().add("sidebarCheckBox");

        return checkBox;
    }

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

    private SearchBox createSearchBox() {
        return new SearchBox(getControl().searchTermProperty());
    }

    private ListWidgetSelector createListWidgetSelector() {
        return new ListWidgetSelector(getControl().selectedListWidgetProperty());
    }
}
