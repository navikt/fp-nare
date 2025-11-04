package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (IKKE) of the given spec.
 */
public class LabelSpecification<T> extends AbstractSpecification<T> {

    public static <V> LabelSpecification<V> label(final Specification<V> spec1, String label) {
        return new LabelSpecification<V>(spec1, label);
    }

    private Specification<T> spec1;
    private String label;

    public LabelSpecification(final Specification<T> spec1, String label) {
        this(spec1, null, label);
    }

    public LabelSpecification(final Specification<T> spec1, String id, String label) {
        super(id);
        this.spec1 = spec1;
        this.label = label;
    }

    @Override
    public String beskrivelse() {
        return label;
    }

    @Override
    public Evaluation evaluate(final T t) {
        return spec1.evaluate(t);
    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> "(" + spec1.identifikator() + ")");
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.LABEL, identifikator(), beskrivelse(), spec1.ruleDescription());
    }

}
