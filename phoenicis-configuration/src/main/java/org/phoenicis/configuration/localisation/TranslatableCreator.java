package org.phoenicis.configuration.localisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this if your {@link Translatable} class has a simple constructor.
 * Annotate the constructor with this annotation.
 * The parameters of the constructor has to be named with either {@link ParameterName} or {@link com.fasterxml.jackson.annotation.JsonProperty}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface TranslatableCreator {

}
