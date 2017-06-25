package org.phoenicis.javafx.views.common.widgets.lists;

import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.engines.dto.EngineVersionDTO;
import org.phoenicis.library.dto.ShortcutDTO;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class containing all information needed for an entry in a {@link ListWidget}.
 *
 * @author marc
 * @since 15.05.17
 */
public class ListWidgetEntry<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListWidgetEntry.class);

    /**
     * The object to which the other information belongs
     */
    private E item;

    /**
     * A fallback icon uri, which is used when <code>iconUri</code> is empty
     */
    private URI defaultIconUri;

    /**
     * An uri referencing to a miniature for this entry
     */
    private Optional<URI> iconUri;

    /**
     * The title string belonging to this entry
     */
    private String title;

    /**
     * An optional list of additional information for this entry.
     * These information are only shown inside a {@link org.phoenicis.javafx.views.common.widgets.lists.compact.CompactListWidget} or a
     * {@link org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget}
     */
    private Optional<List<AdditionalListWidgetInformation>> additionalInformation;

    /**
     * An optional list of additional detailed information for this entry.
     * These information are only shown inside a {@link org.phoenicis.javafx.views.common.widgets.lists.details.DetailsListWidget}
     */
    private Optional<List<AdditionalListWidgetInformation>> detailedInformation;

    /**
     * True if this entry is enabled
     */
    private boolean enabled;

    /**
     * Constructor.
     * This constructor assumes that the entry is enabled
     *
     * @param item                  The item from which the entry should be created
     * @param iconUri               An optional uri to a miniature to this entry
     * @param defaultIconUri        An uri to a fallback miniature
     * @param title                 The title to this entry
     * @param additionalInformation An optional list of additional information to this entry
     * @param detailedInformation   An optional list of additional detailed information to this entry
     */
    public ListWidgetEntry(E item, Optional<URI> iconUri, URI defaultIconUri, String title,
            Optional<List<AdditionalListWidgetInformation>> additionalInformation,
            Optional<List<AdditionalListWidgetInformation>> detailedInformation) {
        this(item, iconUri, defaultIconUri, title, additionalInformation, detailedInformation, true);
    }

    /**
     * Constructor
     *
     * @param item                  The item from which the entry should be created
     * @param iconUri               An optional uri to a miniature to this entry
     * @param defaultIconUri        An uri to a fallback miniature
     * @param title                 The title to this entry
     * @param additionalInformation An optional list of additional information to this entry
     * @param detailedInformation   An optional list of additional detailed information to this entry
     * @param enabled               True if this entry is enabled
     */
    public ListWidgetEntry(E item, Optional<URI> iconUri, URI defaultIconUri, String title,
            Optional<List<AdditionalListWidgetInformation>> additionalInformation,
            Optional<List<AdditionalListWidgetInformation>> detailedInformation, boolean enabled) {
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
        return new ListWidgetEntry<>(application, application.getMainMiniature(), StaticMiniature.DEFAULT_MINIATURE,
                application.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<ContainerDTO> create(ContainerDTO container) {
        Optional<URI> shortcutMiniature = Optional.empty();

        // create a miniature by composing the miniatures of the shortcuts using this container
        final List<BufferedImage> miniatures = new ArrayList<>();
        // do not use too many segments (cannot recognize the miniature if the segment is too small)
        final int maxSegments = 4;
        int currentSegment = 0;
        for (ShortcutDTO shortcutDTO : container.getInstalledShortcuts()) {
            if (currentSegment >= maxSegments) {
                break;
            }
            try {
                miniatures.add(ImageIO.read(shortcutDTO.getMiniature().toURL()));
                currentSegment++;
            } catch (IOException e) {
                LOGGER.warn(String.format("Could not read miniature for shortcut \"%s\"", shortcutDTO.getName()), e);
            }
        }

        if (!miniatures.isEmpty()) {
            // assumption: all miniatures have the same dimensions
            final int width = miniatures.get(0).getWidth();
            final int height = miniatures.get(0).getHeight();

            BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final int numberOfSegments = miniatures.size();
            final int segmentWidth = width / numberOfSegments;
            // get segments from the miniatures (part around the center)
            final int offset = (width - segmentWidth) / 2;
            final List<BufferedImage> segments = new ArrayList<>();
            miniatures.forEach(
                    miniature -> segments.add(miniature.getSubimage(offset, 0, offset + segmentWidth, height)));
            // compose the segments
            Graphics2D graphics = result.createGraphics();
            for (int i = 0; i < segments.size(); i++) {
                graphics.drawImage(segments.get(i), 0 + i * segmentWidth, 0, null);
            }

            try {
                final Path temp = Files.createTempFile(container.getName(), ".png").toAbsolutePath();
                final File tempFile = temp.toFile();
                tempFile.deleteOnExit();
                ImageIO.write(result, "png", tempFile);
                shortcutMiniature = Optional.of(temp.toUri());
            } catch (IOException e) {
                LOGGER.warn(
                        String.format("Could not create container miniature for container \"%s\"", container.getName()),
                        e);
            }
        }

        return new ListWidgetEntry<>(container, shortcutMiniature, StaticMiniature.CONTAINER_MINIATURE,
                container.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<ShortcutDTO> create(ShortcutDTO shortcut) {
        return new ListWidgetEntry<>(shortcut, Optional.ofNullable(shortcut.getMiniature()),
                StaticMiniature.DEFAULT_MINIATURE, shortcut.getName(), Optional.empty(), Optional.empty());
    }

    public static ListWidgetEntry<EngineVersionDTO> create(EngineVersionDTO engineVersion, boolean installed) {
        return new ListWidgetEntry<>(engineVersion, Optional.empty(), StaticMiniature.WINE_MINIATURE,
                engineVersion.getVersion(), Optional.empty(), Optional.empty(), installed);
    }

    /**
     * Returns the item belonging to this entry
     *
     * @return The item belonging to this entry
     */
    public E getItem() {
        return this.item;
    }

    /**
     * Returns an uri to a miniature icon for this entry
     *
     * @return An uri to a miniature icon for this entry
     */
    public URI getIconUri() {
        return this.iconUri.orElse(defaultIconUri);
    }

    /**
     * Returns the title for this entry
     *
     * @return The title for this entry
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the additional information for this entry
     *
     * @return The additional information for this entry
     */
    public Optional<List<AdditionalListWidgetInformation>> getAdditionalInformation() {
        return this.additionalInformation;
    }

    /**
     * Returns the additional detailed information for this entry
     *
     * @return The additional detailed information for this entry
     */
    public Optional<List<AdditionalListWidgetInformation>> getDetailedInformation() {
        return this.detailedInformation;
    }

    /**
     * Returns if this entry is enabled
     *
     * @return True if this entry is enabled, false otherwise
     */
    public boolean isEnabled() {
        return this.enabled;
    }
}
