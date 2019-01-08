package org.phoenicis.javafx.components.installation.skin;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import org.phoenicis.javafx.components.common.skin.DetailsPanelBaseSkin;
import org.phoenicis.javafx.components.installation.control.InstallationDetailsPanel;
import org.phoenicis.javafx.views.mainwindow.installations.dto.InstallationDTO;

/**
 * The skin for the {@link InstallationDetailsPanel} component
 */
public class InstallationDetailsPanelSkin
        extends DetailsPanelBaseSkin<InstallationDetailsPanel, InstallationDetailsPanelSkin> {
    /**
     * The content panel belonging to the shown installation
     */
    private final ObjectProperty<Node> installationPanel;

    /**
     * Constructor
     *
     * @param control The control belonging to the skin
     */
    public InstallationDetailsPanelSkin(InstallationDetailsPanel control) {
        super(control);

        this.installationPanel = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialise() {
        super.initialise();

        // ensure that the content of the details panel changes when the to be shown installation changes
        getControl().installationProperty().addListener((Observable invalidation) -> updateInstallation());
        // initialize the content of the details panel correctly
        updateInstallation();
    }

    /**
     * {@inheritDoc}
     * <p>
     * TODO: Currently I need to add an additional inner container to bind the installationPanel property to.
     * TODO: It would be better to bind it directly to the center property of the outer BorderPane container
     */
    @Override
    protected Node createContent() {
        final BorderPane container = new BorderPane();

        container.centerProperty().bind(installationPanel);

        return container;
    }

    /**
     * Update the content of the details panel when the shown installation changes
     */
    private void updateInstallation() {
        final InstallationDTO installation = getControl().getInstallation();

        if (installation != null) {
            title.setValue(installation.getName());
            installationPanel.setValue(installation.getNode());
        }
    }
}
