package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.AndEvaluation;

/**
 * AND specification, used to create a new specifcation that is the AND of two other specifications.
 */
public class AndSpecification<T> extends BinarySpecification<T> {

    public AndSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        super(spec1, spec2);
    }

    @Override
    public String beskrivelse() {
        String beskrivelse = super.beskrivelse();
        if (beskrivelse.isEmpty()) {
            return "(" + spec1.beskrivelse() + " OG " + spec2.beskrivelse() + ")";
        } else {
            return beskrivelse;
        }
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new AndEvaluation(identifikator(), beskrivelse(), spec1.evaluate(t), spec2.evaluate(t));
    }

    @Override
    public String identifikator() {
        String id = super.identifikator();
        if (id.isEmpty()) {
            return "(" + spec1.identifikator() + " OG " + spec2.identifikator() + ")";
        } else {
            return id;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.AND, identifikator(), beskrivelse(), spec1.ruleDescription(), spec2.ruleDescription());
    }

}
