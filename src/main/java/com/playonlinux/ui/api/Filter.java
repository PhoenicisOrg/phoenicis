package com.playonlinux.ui.api;

import java.util.Observer;

public interface Filter<DTO> {

    void addObserver(Observer o);

    /**
     * Test the given script against the filter rules.
     * <p>
     *     Note: At the moment, this method does not filter categories, since Scripts don't know about their own category.
     * </p>
     * @param dto DTO which should be tested against the filter.
     * @return True if this Script matches the filter criteria, false otherwise.
     */
    boolean apply(DTO dto);

}
