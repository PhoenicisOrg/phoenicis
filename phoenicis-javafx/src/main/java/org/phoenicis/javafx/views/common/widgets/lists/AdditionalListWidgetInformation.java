package org.phoenicis.javafx.views.common.widgets.lists;

/**
 * Created by marc on 15.05.17.
 */
public class AdditionalListWidgetInformation {
    /**
     * The content of this information object
     */
    private final String content;

    /**
     * In percent
     */
    private final int width;

    public AdditionalListWidgetInformation(String content, int width) {
        this.content = content;
        this.width = width;
    }

    public String getContent() {
        return content;
    }

    public int getWidth() {
        return width;
    }
}
