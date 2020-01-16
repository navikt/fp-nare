package no.nav.fpsak.nare.specification;

public interface SpecificationVisitor<T> {

    void visiting(Specification<T> parentSpecification, Specification<T> childSpecification);
}
