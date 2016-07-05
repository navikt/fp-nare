package no.nav.aura.nare.specifications.common;


/**
 * AND specification, used to create a new specifcation that is the AND of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;

    /**
     * Create a new AND specification based on two other spec.
     * 
     * @param spec1
     *            Specification one.
     * @param spec2
     *            Specification two.
     */
    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public String getDescription() {
        return spec1.getDescription() + " OG " + spec2.getDescription();
    }

    /**
     * {@inheritDoc}
     */
    public Evaluation evaluate(final T t) {
        Evaluation eval1 = spec1.evaluate(t);
        Evaluation eval2 = spec2.evaluate(t);
        return new Evaluation(eval2.result().and(eval1.result()), getReason(eval1, eval2));

    }

    private String getReason(Evaluation eval1, Evaluation eval2) {
        return "(" + eval1.result() + ": " + eval1.getReason()+ ")" + "  OG  " + "(" + eval2.result() + ": " +  eval2.getReason() + ")";
    }
}
