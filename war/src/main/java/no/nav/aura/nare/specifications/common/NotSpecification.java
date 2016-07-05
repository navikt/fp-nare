package no.nav.aura.nare.specifications.common;



/**
 * NOT decorator, used to create a new specifcation that is the inverse (IKKE) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    public static NotSpecification ikke(final Specification spec1){
        return  new NotSpecification(spec1);
    }

    /**
     * Create a new NOT specification based on another spec.
     *
     * @param spec1
     *            Specification instance to not.
     *
     */
    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    /**
     * {@inheritDoc}
     */
    public Evaluation evaluate(final T t) {
        Evaluation eval1 = spec1.evaluate(t);
        Evaluation evaluation = new Evaluation(eval1.result().not(), getReason(eval1));
        return evaluation;
    }

    private String getReason(Evaluation eval1) {
        return eval1.result().not() + ":" + eval1.getReason();
    }

    @Override
    public String getDescription() {
        return "IKKE (" + spec1.getDescription() + ")";
    }
}
