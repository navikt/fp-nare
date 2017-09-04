package no.nav.fpsak.nare.specification;

public abstract class BinarySpecification<T> extends AbstractSpecification<T> {

    protected Specification<T> spec1;
    protected Specification<T> spec2;

    public BinarySpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }
    
    @Override
    public void visit(Specification<T> parentSpecification, SpecificationVisitor<T> visitor) {
        visitor.visiting(parentSpecification, this);
        spec1.visit(this, visitor);
        spec2.visit(this, visitor);
    }
}
