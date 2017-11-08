package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.SequenceEvaluation;

/**
 * SEQUENCE specification, used to create a new specification that is a sequence of two other specifications,
 * ignoring the result of the first specification (ie assumed always JA)
 */
public class SequenceSpecification<T> extends BinarySpecification<T> {

    public SequenceSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        super(spec1, spec2);
    }

    @Override
    public String beskrivelse() {
        String beskrivelse = super.beskrivelse();
        if (beskrivelse.isEmpty()) {
            return "(" + spec1.beskrivelse() + " FULGT AV " + spec2.beskrivelse() + ")";
        } else {
            return beskrivelse;
        }
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new SequenceEvaluation(identifikator(), beskrivelse(), spec1.evaluate(t), spec2.evaluate(t));
    }

    @Override
    public String identifikator() {
        String id = super.identifikator();
        if (id.isEmpty()) {
            return "(" + spec1.identifikator() + " FULGT AV " + spec2.identifikator() + ")";
        } else {
            return id;
        }
    }

    @Override
    public RuleDescription ruleDescription() {
        return new RuleDescription(Operator.AND, identifikator(), beskrivelse(), spec1.ruleDescription(), spec2.ruleDescription());
    }
}
