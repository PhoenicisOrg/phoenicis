package org.phoenicis.javafx.components.setting.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.AboutPanelSkin;
import org.phoenicis.javafx.components.setting.utils.ApplicationBuildInformation;
import org.phoenicis.tools.system.opener.Opener;

/**
 * An implementation of the "about" panel inside the settings tab
 */
public class AboutPanel extends ControlBase<AboutPanel, AboutPanelSkin> {
    /**
     * The {@link Opener} instance used to open a browser
     */
    private final Opener opener;

    /**
     * The shown build information of Phoenicis
     */
    private final ObjectProperty<ApplicationBuildInformation> buildInformation;

    /**
     * Constructor
     *
     * @param opener The {@link Opener} instance used to open a browser
     * @param buildInformation The shown build information of Phoenicis
     */
    public AboutPanel(Opener opener, ObjectProperty<ApplicationBuildInformation> buildInformation) {
        super();

        this.opener = opener;
        this.buildInformation = buildInformation;
    }

    /**
     * Constructor
     *
     * @param opener The {@link Opener} instance used to open a browser
     * @param buildInformation The shown build information of Phoenicis
     */
    public AboutPanel(Opener opener, ApplicationBuildInformation buildInformation) {
        this(opener, new SimpleObjectProperty<>(buildInformation));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AboutPanelSkin createSkin() {
        return new AboutPanelSkin(this);
    }

    public Opener getOpener() {
        return opener;
    }

    public ApplicationBuildInformation getBuildInformation() {
        return buildInformation.get();
    }

    public ObjectProperty<ApplicationBuildInformation> buildInformationProperty() {
        return buildInformation;
    }

    public void setBuildInformation(ApplicationBuildInformation buildInformation) {
        this.buildInformation.set(buildInformation);
    }
}
