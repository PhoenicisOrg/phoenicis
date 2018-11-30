package org.phoenicis.javafx.components.skin;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.phoenicis.javafx.components.control.SidebarGroup;
import org.phoenicis.javafx.views.common.lists.AdhocList;

public class SidebarGroupSkin extends SkinBase<SidebarGroup, SidebarGroupSkin> {
    /**
     * Constructor
     *
     * @param control The control for which this Skin should attach to.
     */
    public SidebarGroupSkin(SidebarGroup control) {
        super(control);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        VBox container = new VBox();
        container.getStyleClass().add("sidebarInside");

        Bindings.bindContent(container.getChildren(),
                new AdhocList<>(getControl().getComponents(), createTitleLabel()));

        getChildren().addAll(container);
    }

    private Label createTitleLabel() {
        Label title = new Label();
        title.getStyleClass().add("sidebarTitle");

        title.textProperty().bind(getControl().titleProperty());
        // only make the title label visible if the property has been set
        title.visibleProperty().bind(Bindings.isNotNull(getControl().titleProperty()));

        return title;
    }
}
