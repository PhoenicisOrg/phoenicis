package org.phoenicis.javafx.components.container.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.javafx.components.common.control.ControlBase;
import org.phoenicis.javafx.components.container.skin.ContainerOverviewPanelSkin;

import java.util.function.Consumer;

/**
 * A component used to show generic information for a selected container
 */
public class ContainerOverviewPanel extends ControlBase<ContainerOverviewPanel, ContainerOverviewPanelSkin> {
    /**
     * The selected container
     */
    private final ObjectProperty<WinePrefixContainerDTO> container;

    /**
     * The callback method which is called when the container should be deleted
     */
    private final ObjectProperty<Consumer<ContainerDTO>> onDeleteContainer;

    /**
     * The callback method which is called when the engine version of the container should be changed
     */
    private final ObjectProperty<Consumer<ContainerDTO>> onChangeEngineVersion;

    /**
     * The callback method which is called when the container should be opened in a file browser
     */
    private final ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowser;

    /**
     * Constructor
     */
    public ContainerOverviewPanel() {
        super();

        this.container = new SimpleObjectProperty<>();

        this.onDeleteContainer = new SimpleObjectProperty<>();
        this.onChangeEngineVersion = new SimpleObjectProperty<>();
        this.onOpenFileBrowser = new SimpleObjectProperty<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerOverviewPanelSkin createSkin() {
        return new ContainerOverviewPanelSkin(this);
    }

    public WinePrefixContainerDTO getContainer() {
        return this.container.get();
    }

    public ObjectProperty<WinePrefixContainerDTO> containerProperty() {
        return this.container;
    }

    public void setContainer(WinePrefixContainerDTO container) {
        this.container.set(container);
    }

    public Consumer<ContainerDTO> getOnDeleteContainer() {
        return this.onDeleteContainer.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onDeleteContainerProperty() {
        return this.onDeleteContainer;
    }

    public void setOnDeleteContainer(Consumer<ContainerDTO> onDeleteContainer) {
        this.onDeleteContainer.set(onDeleteContainer);
    }

    public Consumer<ContainerDTO> getOnChangeEngineVersion() {
        return this.onChangeEngineVersion.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onChangeEngineVersionProperty() {
        return this.onChangeEngineVersion;
    }

    public void setOnChangeEngineVersion(Consumer<ContainerDTO> onChangeEngineVersion) {
        this.onChangeEngineVersion.set(onChangeEngineVersion);
    }

    public Consumer<ContainerDTO> getOnOpenFileBrowser() {
        return this.onOpenFileBrowser.get();
    }

    public ObjectProperty<Consumer<ContainerDTO>> onOpenFileBrowserProperty() {
        return this.onOpenFileBrowser;
    }

    public void setOnOpenFileBrowser(Consumer<ContainerDTO> onOpenFileBrowser) {
        this.onOpenFileBrowser.set(onOpenFileBrowser);
    }
}
