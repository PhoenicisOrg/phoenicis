package com.playonlinux.utils;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplacableProperties extends Properties {
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

    private String replaceGlobalVariables(String rawProperty) {
        return matchAndReplace(rawProperty, "\\$\\(([^\\}]*)\\)", System.getProperties());
    }

    private String replaceLocalVariables(String rawProperty) {
        return matchAndReplace(rawProperty, "\\$\\{([^\\}]*)\\}", this);
    }


}
