package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.NotEvaluation;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (IKKE) of the given spec.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

    public static <V> NotSpecification<V> ikke(final Specification<V> spec1) {
        return new NotSpecification<V>(spec1);
    }

    private Specification<T> spec1;

    public NotSpecification(final Specification<T> spec1) {
        this.spec1 = spec1;
    }

    @Override
    public String beskrivelse() {
        return beskrivelseIkkeTom().orElse("(IKKE)");
    }

    @Override
    public Evaluation evaluate(final T t) {
        return new NotEvaluation(identifikator(), beskrivelse(), spec1.evaluate(t));
    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> "(IKKE " + spec1.identifikator() + ")");
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.NOT, identifikator(), beskrivelse(), spec1.ruleDescription());
    }

}
