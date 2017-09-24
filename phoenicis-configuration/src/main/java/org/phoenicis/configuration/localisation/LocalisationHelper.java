package org.phoenicis.configuration.localisation;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

class LocalisationHelper {
    /**
     * Given a parameter name annotated with either {@link ParameterName} or {@link JsonProperty},
     * returns the name of a corresponding getter
     * @param parameter The parameter
     * @return The name of a corresponding getter
     */
    String getterNameFromParameter(Parameter parameter) {
        final String getterPrefix;
        if (parameter.getType() == Boolean.class || parameter.getType() == boolean.class) {
            getterPrefix = "is";
        } else {
            getterPrefix = "get";
        }
        return getterPrefix + toCamelCase(fetchParameterName(parameter));
    }

    /**
     * Given a builder method having a name starting by "with"
     * returns the name of a corresponding getter
     * @param method The method
     * @return The name of a corresponding getter
     */
    String getterNameFromBuilderMethod(Method method) {
        final String getterPrefix;
        if (Arrays.stream(method.getParameterTypes())
                .anyMatch(clazz -> clazz == Boolean.class || clazz == boolean.class)) {
            getterPrefix = "is";
        } else {
            getterPrefix = "get";
        }
        return getterPrefix + toCamelCase(method.getName().replace("with", ""));
    }

    private String fetchParameterName(Parameter parameter) {
        final JsonProperty jsonAnnotation = parameter.getAnnotation(JsonProperty.class);
        final ParameterName parameterNameAnnotation = parameter.getAnnotation(ParameterName.class);

        if (parameterNameAnnotation != null) {
            return parameterNameAnnotation.value();
        }

        if (jsonAnnotation != null) {
            return jsonAnnotation.value();
        }

        return parameter.getName();
    }

    private String toCamelCase(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        stringBuilder.replace(0, 1, string.substring(0, 1).toUpperCase());
        return stringBuilder.toString();
    }
}
