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
     * @return option
     */
    String getCurrentOption();

    /**
     * sets the selected option
     * @param container name of container for which the setting shall be applied
     * @param option selected option
     */
    void setOption(String container, String option);
}
