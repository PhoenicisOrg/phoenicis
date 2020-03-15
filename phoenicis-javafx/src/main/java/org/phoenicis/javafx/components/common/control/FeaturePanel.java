package org.phoenicis.javafx.components.common.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Tab;
import org.phoenicis.javafx.components.common.skin.FeaturePanelSkin;

/**
 * The {@link FeaturePanel} is the core component of the Phoenicis JavaFX GUI.
 * <p>
 * It encloses common functionality (e.g. the library) inside a {@link Tab} which is presented in the main menu.
 * Every {@link FeaturePanel} is split into three basic components: a sidebar, the content and an optional details
 * panel.
 * <p>
 * The Sidebar is shown on the left side. It shall allow for a quick navigation in the categories which structure the
 * content.
 * <p>
 * The content is the main content which presents the functionality of this particular {@link FeaturePanel}.
 * <p>
 * The details panel is an optional panel on the right side which can be opened to show details about currently selected
 * items in the content section.
 *
 * @param <C> The control type of the component
 * @param <S> The skin type of the component
 */
public abstract class FeaturePanel<C extends FeaturePanel<C, S>, S extends FeaturePanelSkin<C, S>>
        extends ControlBase<C, S> {
    /**
     * A property containing the initialization state of this view
     */
    private final BooleanProperty initialized;

    /**
     * Constructor
     *
     * @param initialized A property containing the initialization state of this view
     */
    protected FeaturePanel(BooleanProperty initialized) {
        super();

        this.initialized = initialized;
    }

    /**
     * Constructor
     */
    protected FeaturePanel() {
        this(new SimpleBooleanProperty(false));
    }

    public boolean isInitialized() {
        return this.initialized.get();
    }

    public BooleanProperty initializedProperty() {
        return this.initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized.set(initialized);
    }

    /**
     * Closes the currently opened details panel.
     * If no details panel has been opened nothing happens
     */
    public abstract void closeDetailsPanel();
}
