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

package org.phoenicis.scripts.nashorn;

import org.phoenicis.scripts.interpreter.InteractiveScriptSession;
import org.phoenicis.scripts.interpreter.ScriptInterpreter;

import java.util.Locale;
import java.util.function.Consumer;

public class NashornScriptInterpreter implements ScriptInterpreter {
    private final NashornEngineFactory nashornEngineFactory;

    public NashornScriptInterpreter(NashornEngineFactory nashornEngineFactory) {
        this.nashornEngineFactory = nashornEngineFactory;
    }

    @Override
    public void runScript(String scriptContent, Consumer<Exception> errorCallback) {
        final String language = Locale.getDefault().getLanguage();
        final String i18nextInit = "i18next.init({                                                                     "
                + String.format("  lng: '%s',                                                             ", language)
                + "  debug: true,                                                                                      "
                + "  resources: {                                                                                      "
                + "    en: {                                                                                           "
                + "      translation: {                                                                                "
                + "        \"Please enter the name of your application\": \"Please enter the name of your application.\""
                + "      }                                                                                             "
                + "    },                                                                                              "
                + "    de: {                                                                                           "
                + "      translation: {                                                                                "
                + "        \"Please enter the name of your application\": \"Bitte geben Sie den Namen Ihrer Anwendung ein.\""
                + "      }                                                                                             "
                + "    }                                                                                               "
                + "  }                                                                                                 "
                + "}, function(err, t) {                                                                               "
                + "    if (err) {                                                                                      "
                + "        print('Could not load translation', err);                                                   "
                + "    }                                                                                               "
                + "});                                                                                                 ";
        final String scriptContenti18n = "include([\"Functions\", \"i18n\", \"i18next\"]);" + i18nextInit
                + scriptContent;
        nashornEngineFactory.createEngine().eval(scriptContenti18n, errorCallback);
    }

    @Override
    public InteractiveScriptSession createInteractiveSession() {
        return new NashornInteractiveSession(nashornEngineFactory);
    }

}
