package org.phoenicis.configuration.localisation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tells Phoenicis localisation module that the annotated class can be translated.
 * This requires:
 * <ul>
 *     <li>{@link TranslatableCreator} or {@link TranslatableBuilder}</li>
 *     <li>At least one getter to be annotated with {@link Translate}</li>
 * </ul>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Translatable {

}
