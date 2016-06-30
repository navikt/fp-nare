package no.nav.aura.nare.specifications.common;


/**
 * Specificaiton interface.
 * <p/>
 * Use {@link AbstractSpecification} as base for creating specifications, og only the method {@link #evaluate(Object)} must be
 * implemented.
 */
public interface Specification<T> {

    /**
     * Check if {@code t} is satisfied by the specification.
     * 
     * @param t
     *            Object to test.
     * @return {@code true} if {@code t} satisfies the specification.
     */
    Evaluation evaluate(T t);

    String getDescription();

    /**
     * Create a new specification that is the AND operation of {@code this} specification og another specification.
     * 
     * @param specification
     *            Specification to AND.
     * @return A new specification.
     */
    Specification<T> og(Specification<T> specification);

    /**
     * Create a new specification that is the OR operation of {@code this} specification og another specification.
     * 
     * @param specification
     *            Specification to OR.
     * @return A new specification.
     */
    Specification<T> eller(Specification<T> specification);

    /**
     * Create a new specification that is the NOT operation of {@code this} specification.
     * 
     * @param specification
     *            Specification to NOT.
     * @return A new specification.
     */
    Specification<T> ikke(Specification<T> specification);
}
