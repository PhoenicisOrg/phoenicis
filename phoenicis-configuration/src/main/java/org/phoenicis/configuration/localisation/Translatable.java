package org.phoenicis.configuration.localisation;

/**
 * interface for objects which can be translated via tr()
 * e.g. implemented by the DTOs to translate application name and description
 * @param <E>
 */
public interface Translatable<E extends Translatable<?>> {

    /**
     * translate the object
     */
    E translate();
}
