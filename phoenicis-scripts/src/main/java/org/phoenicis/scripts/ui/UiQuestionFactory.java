/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.scripts.ui;

import com.google.common.util.concurrent.Runnables;

public interface UiQuestionFactory {
    /**
     * Creates a question UI (yes/no decision)
     *
     * @param questionText The question text
     * @param yesCallback The callback for when the user selects "yes"
     * @param noCallback The callback for when the user selects "no"
     */
    void create(String questionText, Runnable yesCallback, Runnable noCallback);

    /**
     * Creates a question UI (yes/no decision)
     *
     * @param questionText The question text
     * @return True if the user selects "yes", false if the user selects "no"
     */
    boolean create(String questionText);

    default void create(String questionText, Runnable yesCallback) {
        create(questionText, yesCallback, Runnables.doNothing());
    }
}
