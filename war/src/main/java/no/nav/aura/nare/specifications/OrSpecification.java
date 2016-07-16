package no.nav.aura.nare.specifications;

import no.nav.aura.nare.evaluation.AggregatedEvaluation;
import no.nav.aura.nare.evaluation.Evaluation;

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
        return AggregatedEvaluation.orEvaluation(spec1.evaluate(t), spec2.evaluate(t));
    }

/*
    private String getReason(Evaluation eval1, Evaluation eval2) {
        return "(" + eval1.result() + ": " + eval1.getReason()+ ")" + "  ELLER  " + "(" + eval2.result() + ": " +  eval2.getReason() + ")";
    }
*/


    @Override
    public String identifikator() {
        return "ELLER";
    }

    @Override
    public String beskrivelse() {
        return "(" + spec1.beskrivelse() + " " + identifikator() + " " +  spec2.beskrivelse() + ")\n ";
    }


}
