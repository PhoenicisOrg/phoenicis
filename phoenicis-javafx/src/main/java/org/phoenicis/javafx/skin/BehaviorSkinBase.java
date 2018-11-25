package org.phoenicis.javafx.skin;

import org.phoenicis.javafx.behavior.BehaviorBase;
import org.phoenicis.javafx.controler.ControlBase;

/**
 * A base class for a JavaFX component skin/view, which also provides separate behavior
 *
 * @param <C> The control/model class belonging to the skin
 * @param <S> The skin/model class itself
 * @param <B> The behavior/controller class belonging to the skin
 */
public abstract class BehaviorSkinBase<C extends ControlBase<C, S>, S extends BehaviorSkinBase<C, S, B>, B extends BehaviorBase<C, S, B>>
        extends SkinBase<C, S> {
    /**
     * The behavior associated to this skin
     */
    private B behavior;

    /**
     * Constructor for all BehaviorSkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    protected BehaviorSkinBase(C control) {
        super(control);

        // create the behavior
        this.behavior = createBehavior();
    }

    /**
     * Creates the default behavior for this skin
     *
     * @return The default behavior for this skin
     */
    public abstract B createBehavior();

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        super.dispose();

        if (behavior != null) {
            // cleanup the behavior
            behavior.dispose();
        }

        // remove reference to the behavior
        this.behavior = null;
    }
}
