package no.nav.aura.nare.specifications;


import no.nav.aura.nare.RuleDescription;
import no.nav.aura.nare.evaluation.AggregatedEvaluation;
import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.evaluation.NotEvaluation;
import no.nav.aura.nare.evaluation.Operator;

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
        return new NotEvaluation(identifikator(), beskrivelse(), spec1.evaluate(t));
    }


    @Override
    public String beskrivelse() {
        if (beskrivelse.isEmpty()){
            return "(IKKE " + spec1.beskrivelse() + ")";
        }else{
            return beskrivelse;
        }

    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.NOT, identifikator(), beskrivelse(), spec1.ruleDescription());
    }
}
