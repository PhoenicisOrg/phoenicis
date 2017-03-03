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

package org.phoenicis.configuration.localisation;

/**
 * Interface to translate UI strings to different languages.
 */
public interface LanguageBundle {
    /**
     * Try to translate the given message in with this bundle in the default
     * context.
     * 
     * @param toTranslate
     *            The string to translate.
     * @return The translated string or <code>toTranslate</code> if this string
     *         could not be translated with this bundle. Never null.
     * @throws NullPointerException
     *             if toTranslate is null.
     */
    String translate(String toTranslate);

    /**
     * Try to translate the given message in the given context with this bundle.
     * 
     * @param toTranslate
     *            The string to translate.
     * @param context
     *            The context ID in which to translate this string. May be null.
     * @return The translated string or <code>toTranslate</code> if this string
     *         could not be translated with this bundle. Never null.
     * @throws NullPointerException
     *             if toTranslate is null.
     */
    String translate(String context, String toTranslate);
}
