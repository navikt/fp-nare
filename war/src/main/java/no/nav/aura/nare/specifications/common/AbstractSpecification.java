package no.nav.aura.nare.specifications.common;

/**
 * Abstract base implementation of composite {@link Specification} with default implementations for {@code og}, {@code eller} og
 * {@code not}.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {


    protected AbstractSpecification(){}

    /**
     * {@inheritDoc}
     */
    public Specification<T> og(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    /**
     * {@inheritDoc}
     */
    public Specification<T> eller(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

}
