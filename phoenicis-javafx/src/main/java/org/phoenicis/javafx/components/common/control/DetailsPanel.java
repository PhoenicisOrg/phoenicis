package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import org.phoenicis.javafx.components.common.skin.DetailsPanelSkin;

/**
 * A details panel component consisting of a title, close button and a content
 */
public class DetailsPanel extends ControlBase<DetailsPanel, DetailsPanelSkin> {
    /**
     * The title of the details panel
     */
    private final StringProperty title;

    /**
     * The content of the details panel
     */
    private final ObjectProperty<Node> content;

    /**
     * Callback for close button clicks
     */
    private final ObjectProperty<Runnable> onClose;

    /**
     * Constructor
     *
     * @param title The title of the details panel
     * @param content The content of the details panel
     * @param onClose Callback for close button clicks
     */
    public DetailsPanel(StringProperty title, ObjectProperty<Node> content, ObjectProperty<Runnable> onClose) {
        super();

        this.title = title;
        this.content = content;
        this.onClose = onClose;
    }

    /**
     * Constructor
     */
    public DetailsPanel() {
        this(new SimpleStringProperty(), new SimpleObjectProperty<>(), new SimpleObjectProperty<>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DetailsPanelSkin createSkin() {
        return new DetailsPanelSkin(this);
    }

    public String getTitle() {
        return this.title.get();
    }

    public StringProperty titleProperty() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public Node getContent() {
        return this.content.get();
    }

    public ObjectProperty<Node> contentProperty() {
        return this.content;
    }

    public void setContent(Node content) {
        this.content.set(content);
    }

    public Runnable getOnClose() {
        return this.onClose.get();
    }

    public ObjectProperty<Runnable> onCloseProperty() {
        return this.onClose;
    }

    public void setOnClose(Runnable onClose) {
        this.onClose.set(onClose);
    }
}
