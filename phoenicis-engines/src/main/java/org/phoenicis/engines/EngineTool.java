package org.phoenicis.engines;

/**
 * interface which must be implemented by all engine tools in Javascript
 */
public interface EngineTool {
    /**
     * runs the tool in the given container
     * @param container container where the tool shall be run
     */
    void run(String container);
}
