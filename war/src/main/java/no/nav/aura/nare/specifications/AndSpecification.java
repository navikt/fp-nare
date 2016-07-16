package no.nav.aura.nare.specifications;


import no.nav.aura.nare.evaluation.AggregatedEvaluation;
import no.nav.aura.nare.evaluation.Evaluation;

/**
 * AND specification, used to create a new specifcation that is the AND of two other specifications.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;


    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    public Evaluation evaluate(final T t) {
        return AggregatedEvaluation.andEvaluation(spec1.evaluate(t), spec2.evaluate(t));
    }

    @Override
    public String identifikator() {
        return "OG";
    }

    @Override
    public String beskrivelse() {
        return "(" + spec1.beskrivelse() + " " + identifikator() + " " +  spec2.beskrivelse() + ")\n";
    }

}
