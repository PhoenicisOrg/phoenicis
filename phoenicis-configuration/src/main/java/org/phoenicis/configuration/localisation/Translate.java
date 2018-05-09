package org.phoenicis.configuration.localisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotate a getter of a translatable object that will be translated.
 * <ul>
 * <li>If the getter returns a string, it will be directly translated</li>
 * <li>If the getter returns a {@link Translatable}, it will be recursively translated</li>
 * <li>If the getter returns a set or a list, each item of them will be translated</li>
 * <li>In any other case, nothing will happen</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Translate {

}
