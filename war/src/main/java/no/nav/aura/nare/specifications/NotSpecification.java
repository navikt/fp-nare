package no.nav.aura.nare.specifications;


import no.nav.aura.nare.evaluation.AggregatedEvaluation;
import no.nav.aura.nare.evaluation.Evaluation;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (IKKE) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;

    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    public static NotSpecification ikke(final Specification spec1) {
        return new NotSpecification(spec1);
    }

    @Override
    public Evaluation evaluate(final T t) {
        return AggregatedEvaluation.notEvaluation(spec1.evaluate(t));
    }


    @Override
    public String identifikator() {
        return "IKKE";
    }

    @Override
    public String beskrivelse() {
        return "(IKKE " + spec1.beskrivelse() + ")";
    }
}
