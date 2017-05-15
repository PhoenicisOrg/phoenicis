package org.phoenicis.javafx.views.common.widget;

import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.library.dto.ShortcutDTO;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Created by marc on 15.05.17.
 */
public class ListWidgetEntry<E> {
    private E item;

    private URI defaultIconUri;
    private Optional<URI> iconUri;

    private String title;
    private Optional<List<AdditionalListWidgetInformation>> additionalInformation;
    private Optional<List<AdditionalListWidgetInformation>> detailedInformation;

    private boolean enabled;

    public ListWidgetEntry(E item, Optional<URI> iconUri, URI defaultIconUri, String title,
                           Optional<List<AdditionalListWidgetInformation>> additionalInformation, Optional<List<AdditionalListWidgetInformation>> detailedInformation) {
        this(item, iconUri, defaultIconUri, title, additionalInformation, detailedInformation, true);
    }

    public ListWidgetEntry(E item, Optional<URI> iconUri, URI defaultIconUri, String title,
                           Optional<List<AdditionalListWidgetInformation>> additionalInformation, Optional<List<AdditionalListWidgetInformation>> detailedInformation, boolean enabled) {
        super();

        this.item = item;

        this.defaultIconUri = defaultIconUri;
        this.iconUri = iconUri;

        this.title = title;
        this.additionalInformation = additionalInformation;
        this.detailedInformation = detailedInformation;

        this.enabled = enabled;
    }

    public static ListWidgetEntry<ApplicationDTO> create(ApplicationDTO application) {
        Optional<URI> iconPath = Optional.empty();
        if (!application.getMiniatures().isEmpty()) {
            iconPath = Optional.of(application.getMiniatures().get(0));
        }

        return new ListWidgetEntry<ApplicationDTO>(application, iconPath, StaticMiniature.DEFAULT_MINIATURE,
                application.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<ContainerDTO> create(ContainerDTO container) {
        return new ListWidgetEntry<ContainerDTO>(container, Optional.empty(), StaticMiniature.CONTAINER_MINIATURE,
                container.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<ShortcutDTO> create(ShortcutDTO shortcut) {
        return new ListWidgetEntry<ShortcutDTO>(shortcut, Optional.ofNullable(shortcut.getMiniature()),
                StaticMiniature.DEFAULT_MINIATURE, shortcut.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<EngineVersionDTO> create(EngineVersionDTO engineVersion, boolean installed) {
        return new ListWidgetEntry<EngineVersionDTO>(engineVersion,
                Optional.empty(), StaticMiniature.WINE_MINIATURE, engineVersion.getVersion(), Optional.empty(),
                Optional.empty(), installed);
    }

    public E getItem() {
        return this.item;
    }

    public URI getIconUri() {
        return this.iconUri.orElse(defaultIconUri);
    }

    public String getTitle() {
        return this.title;
    }

    public Optional<List<AdditionalListWidgetInformation>> getAdditionalInformation() {
        return this.additionalInformation;
    }

    public Optional<List<AdditionalListWidgetInformation>> getDetailedInformation() {
        return this.detailedInformation;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
