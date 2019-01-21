package org.phoenicis.javafx.components.setting.control;

import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.setting.skin.NetworkPanelSkin;

/**
 * An implementation of the "network" panel inside the settings tab
 */
public class NetworkPanel extends ControlBase<NetworkPanel, NetworkPanelSkin> {
    /**
     * {@inheritDoc}
     */
    @Override
    public NetworkPanelSkin createSkin() {
        return new NetworkPanelSkin(this);
    }
}
