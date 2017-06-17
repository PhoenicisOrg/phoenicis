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

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.util.ResourceBundle;

public final class Localisation {
    private static I18n i18n = null;

    // This is a static class
    private Localisation() {

    }

    private static I18n getI18n() {
        // init only once
        if (i18n == null) {
            i18n = I18nFactory.getI18n(Localisation.class, "Messages");
        }
        return i18n;
    }

    /**
     * add a ResourceBundle containing additional translations for the localisation
     * - typically used to add translations for the scripts
     * - only one additional ResourceBundle can be set at a time (calling the method again will override the old ResourceBundle)
     *
     * @param resourceBundle
     */
    public static void setAdditionalTranslations(ResourceBundle resourceBundle) {
        getI18n().setResources(resourceBundle);
    }

    public static String tr(String text) {
        return text == null ? "" : getI18n().tr(text);
    }

    public static String tr(String text, Object o1) {
        return text == null ? "" : getI18n().tr(text, o1);
    }

    public static String tr(String text, Object o1, Object o2) {
        return text == null ? "" : getI18n().tr(text, o1, o2);
    }

    public static String tr(String text, Object o1, Object o2, Object o3) {
        return text == null ? "" : getI18n().tr(text, o1, o2, o3);
    }

    public static String tr(String text, Object o1, Object o2, Object o3, Object o4) {
        return text == null ? "" : getI18n().tr(text, o1, o2, o3, o4);
    }

    public static String tr(String text, Object[] objects) {
        return text == null ? "" : getI18n().tr(text, objects);
    }

    /**
     * Translates the given Translatable object, by creating a new instance of it with its corresponding translations
     * @param translatable Translatable object
     * @return translated object
     */
    public static Translatable tr(Translatable translatable) {
        return translatable.translate();
    }

}
