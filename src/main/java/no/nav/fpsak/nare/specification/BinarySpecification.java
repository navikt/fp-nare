package no.nav.fpsak.nare.specification;

public abstract class BinarySpecification<T> extends AbstractSpecification<T> {

    protected Specification<T> spec1;
    protected Specification<T> spec2;

    protected BinarySpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

}
