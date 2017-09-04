package no.nav.fpsak.nare.specification;

public abstract class LeafSpecification<T> extends AbstractSpecification<T> {
    
    public LeafSpecification(String id) {
        super(id);
    }

    @Override
    public void visit(Specification<T> parentSpecification, SpecificationVisitor<T> visitor) {
        visitor.visiting(parentSpecification, this);
    }
}
