package org.phoenicis.scripts;

/**
 * function with 3 arguments
 * @param <A> 1. argument type
 * @param <B> 2. argument type
 * @param <C> 3. argument type
 * @param <R> return value type
 */
@FunctionalInterface
public interface TriFunction<A, B, C, R> {

    /**
     * applies the function
     *
     * @param a 1. argument
     * @param b 2. argument
     * @param c 3. argument
     * @return
     */
    R apply(A a, B b, C c);
}
