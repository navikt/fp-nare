package no.nav.aura.nare.specifications.common;

/**
 * OR specification, used to create a new specifcation that is the OR of two other specifications.
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;

    /**
     * Create a new OR specification based on two other spec.
     * 
     * @param spec1
     *            Specification one.
     * @param spec2
     *            Specification two.
     */
    public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    /**
     * {@inheritDoc}
     */
    public Evaluation evaluate(final T t) {
        Evaluation eval1 = spec1.evaluate(t);
        Evaluation eval2 = spec2.evaluate(t);
        return new Evaluation(eval2.result().or(eval1.result()), getReason(eval1, eval2));
    }

    private String getReason(Evaluation eval1, Evaluation eval2) {
        return "(" + eval1.result() + ": " + eval1.getReason()+ ")" + "  ELLER  " + "(" + eval2.result() + ": " +  eval2.getReason() + ")";


    }

    @Override
    public String getDescription() {

        return spec1.getDescription() + " ELLER " + spec2.getDescription();
    }

}
