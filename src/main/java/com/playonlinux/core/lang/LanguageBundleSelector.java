/*
 * Copyright (C) 2015 Jonas Konrad
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

package com.playonlinux.core.lang;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.fedorahosted.tennera.jgettext.PoParser;

public final class LanguageBundleSelector {
    private LanguageBundleSelector() {
        // Utility class
    }

    /**
     * Select a LanguageBundle for the given resource from the classpath.
     *
     * @return The language bundle, never null. May be a {@link FallbackLanguageBundle} which will do a no-operation
     * translation.
     */
    public static LanguageBundle forLocale(Locale locale) {
        // first, try the full language-country tag (either xx-YY or just xx if no country was specified)
        String fullTag = new Locale(locale.getLanguage(), locale.getCountry())
                .toLanguageTag().replace('-', '_');
        LanguageBundle bundle = forLocaleIdOrNull(fullTag);
        if (bundle != null) {
            return bundle;
        }

        // then, try the base language tag (just xx for language)
        String languageTag = new Locale(locale.getLanguage())
                .toLanguageTag().replace('-', '_');
        bundle = forLocaleIdOrNull(languageTag);
        if (bundle != null) {
            return bundle;
        }

        // if neither was found, fall back on defaults
        return FallbackLanguageBundle.getInstance();
    }

    private static LanguageBundle forLocaleIdOrNull(String localeId) {
        URL poResourceUrl = Localisation.class.getResource("/locale/po/" + localeId + ".po");
        if (poResourceUrl == null) {
            return null;
        } else {
            return parseCatalogBundle(poResourceUrl);
        }
    }

    private static LanguageBundle parseCatalogBundle(URL poResourceUrl) {
        PoParser parser = new PoParser();
        try (InputStream inputStream = poResourceUrl.openStream()) {
            return new CatalogLanguageBundle(parser.parseCatalog(inputStream, false));
        } catch (IOException e) {
            // this should only happen if a PO file is broken so we throw an Error
            throw new IOError(e);
        }
    }
}
