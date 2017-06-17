package org.phoenicis.configuration.localisation;

public interface Translatable<E extends Translatable<?>> {

    /**
     * translate the object
     */
    E translate();
}
