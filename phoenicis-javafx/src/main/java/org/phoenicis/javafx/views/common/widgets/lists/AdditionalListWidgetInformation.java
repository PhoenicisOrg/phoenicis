package org.phoenicis.javafx.views.common.widgets.lists;

import org.phoenicis.javafx.components.common.control.CompactListWidget;
import org.phoenicis.javafx.components.common.control.DetailsListWidget;

/**
 * An additional information to be shown in a {@link CompactListWidget} or in a {@link DetailsListWidget}
 *
 * @author marc
 * @since 15.05.17
 */
public class AdditionalListWidgetInformation {
    /**
     * The content of this information object
     */
    private final String content;

    /**
     * The width of the information in percent
     */
    private final int width;

    /**
     * Constructor
     *
     * @param content The content of this information object. This content is then shown in the list widget
     * @param width The width, in percent, this information needs
     */
    public AdditionalListWidgetInformation(String content, int width) {
        this.content = content;
        this.width = width;
    }

    /**
     * Returns the content of this information object
     *
     * @return The content
     */
    public String getContent() {
        return content;
    }

    /**
     * Returns the required width of the content of this information object
     *
     * @return The width
     */
    public int getWidth() {
        return width;
    }
}
