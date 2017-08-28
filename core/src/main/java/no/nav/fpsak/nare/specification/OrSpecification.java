package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.OrEvaluation;

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
    public String identifikator() {
        if (id.isEmpty()) {
            return "(" + spec1.identifikator() + " ELLER " + spec2.identifikator() + ")";
        } else {
            return id;
        }
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