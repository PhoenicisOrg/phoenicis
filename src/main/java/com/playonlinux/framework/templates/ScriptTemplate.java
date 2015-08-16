/*
 * Copyright (C) 2015 PÂRIS Quentin
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

package com.playonlinux.framework.templates;

import com.playonlinux.core.scripts.CancelException;

/**
 * Represents a Script teplate
 */
public interface ScriptTemplate {
    /**
     * Perform some validation before running the script. (Typically configuration checking
     * @return true if the validation is successful, false otherwise
     */
    boolean validate();

    /**
     * Main method of the script
     * @throws CancelException If the script is canceled
     * (by the user, or by a {@link com.playonlinux.framework.ScriptFailureException})
     */
    void main() throws CancelException;

    /**
     * Method to perform if any error occurs during the script
     */
    void rollback();
}
