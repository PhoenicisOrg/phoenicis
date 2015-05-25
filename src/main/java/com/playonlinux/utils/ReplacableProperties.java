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

package com.playonlinux.utils;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplacableProperties extends Properties {
    @Override
    public String getProperty(String key) {
        String rawProperty = super.getProperty(key);
        rawProperty = replaceGlobalVariables(rawProperty);
        rawProperty = replaceLocalVariables(rawProperty);
        return replaceLocalVariables(rawProperty);
    }

    private String matchAndReplace(String rawProperty, String regex, Properties propertyObject) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(rawProperty);
        StringBuffer transformedPropertyStringBuffer = new StringBuffer(rawProperty.length());

        while(matcher.find()) {
            String propertyMatched = matcher.group(1);
            matcher.appendReplacement(transformedPropertyStringBuffer, propertyObject.getProperty(propertyMatched));
        }
        matcher.appendTail(transformedPropertyStringBuffer);

        return transformedPropertyStringBuffer.toString();
    }

    private String replaceGlobalVariables(String inputString) {
        return matchAndReplace(inputString, "\\$\\(([^\\}]*)\\)", System.getProperties());
    }

    private String replaceLocalVariables(String inputString) {
        return matchAndReplace(inputString, "\\$\\{([^\\}]*)\\}", this);
    }

    public String replaceAllVariables(String inputString) {
        String globalVariableTranslatedString = this.replaceGlobalVariables(inputString);

        return this.replaceLocalVariables(globalVariableTranslatedString);
    }
}
