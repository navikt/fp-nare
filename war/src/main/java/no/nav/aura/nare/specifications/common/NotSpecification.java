package no.nav.aura.nare.specifications.common;



/**
 * NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    /**
     * Create a new NOT specification based on another spec.
     * 
     * @param spec1
     *            Specification instance to not.
     */
    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    /**
     * {@inheritDoc}
     */
    public Evaluation evaluate(final T t) {
        Evaluation eval1 = spec1.evaluate(t);
        String reason = eval1.getReason();
        if (!eval1.isSatisfied()) {
            return Evaluation.yes(reason);
        }
        return Evaluation.no(reason);
    }

    @Override
    public String getDescription() {
        return "IKKE (" + spec1.getDescription() + ")";
    }
}
