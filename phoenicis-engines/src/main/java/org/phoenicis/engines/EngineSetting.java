package org.phoenicis.engines;

/**
 * interface which must be implemented by all engine settings in Javascript
 */
public interface EngineSetting {
    /**
     * fetches the text which shall be shown for the setting
     * @return text
     */
    String getText();

    /**
     * fetches the available options for this setting
     * @return options
     */
    String[] getOptions();

    /**
     * fetches the currently used option
     * @param container name of container for which the current setting shall be fetched
     * @return option
     */
    String getCurrentOption(String container);

    /**
     * sets the selected option
     * @param container name of container for which the setting shall be applied
     * @param optionIndex index of selected option
     */
    void setOption(String container, int optionIndex);
}
