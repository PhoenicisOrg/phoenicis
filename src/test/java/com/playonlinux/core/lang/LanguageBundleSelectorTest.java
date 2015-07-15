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

import java.util.Locale;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LanguageBundleSelectorTest {
    @Test
    public void testMissingCountry() {
        // Test a locale has language and country and should fall back on language
        LanguageBundle bundle = LanguageBundleSelector.forLocale(Locale.GERMANY);
        assertEquals("Fehler melden", bundle.translate("Report a bug"));
    }

    @Test
    public void testCountry() {
        // Test a locale has language and country
        LanguageBundle bundle = LanguageBundleSelector.forLocale(new Locale("pt", "br"));
        assertEquals("Relatar um problema (bug)", bundle.translate("Report a bug"));
    }

    @Test
    public void testLanguage() {
        // Test a locale that only has a language
        LanguageBundle bundle = LanguageBundleSelector.forLocale(Locale.GERMAN);
        assertEquals("Fehler melden", bundle.translate("Report a bug"));
    }

    @Test
    public void testFallback() {
        // Test an unsupported locale
        // todo: use a language that doesn't return "undefined" for the language tag? i.e. real language
        LanguageBundle bundle = LanguageBundleSelector.forLocale(new Locale("##", "##"));
        assertEquals("Report a bug", bundle.translate("Report a bug"));
    }
}
