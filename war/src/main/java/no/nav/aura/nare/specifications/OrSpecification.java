package no.nav.aura.nare.specifications;

import no.nav.aura.nare.RuleDescription;
import no.nav.aura.nare.evaluation.AggregatedEvaluation;
import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.evaluation.Operator;
import no.nav.aura.nare.evaluation.OrEvaluation;

/**
 * OR specification, used to create a new specifcation that is the OR of two other specifications.
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec1;
    private Specification<T> spec2;


    public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new OrEvaluation(identifikator(), beskrivelse(),spec1.evaluate(t), spec2.evaluate(t));
    }



    @Override
    public String beskrivelse() {
        if (beskrivelse.isEmpty()) {
            return "(" + spec1.beskrivelse() + " ELLER " + spec2.beskrivelse() + ")";
        } else {
            return beskrivelse;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.OR, identifikator(), beskrivelse(), spec1.ruleDescription(), spec2.ruleDescription());
    }


}
