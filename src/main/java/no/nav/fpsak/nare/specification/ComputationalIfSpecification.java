package no.nav.fpsak.nare.specification;

import java.util.Optional;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.node.ComputationalIfEvaluation;

/**
 * Computational IF specification, used to create a new specification that is a choice between two other specifications.
 *
 * behov for medScope? - den setter kun en property som gir sporingsoutput ifm DynRuleService - ingen annen bruk
 *
 */
public class ComputationalIfSpecification<T> extends AbstractSpecification<T> {

    public static <V> Builder<V> regel(Specification<V> ifSpec) {
        return new Builder<>(ifSpec);
    }

    public static class Builder<T> {
        private Specification<T> testSpec;
        private Specification<T> ifTrueSpec;
        private Specification<T> ifFalseSpec;

        public Builder(Specification<T> ifSpec) {
            this.testSpec = ifSpec;
        }

        public Builder<T> hvisja(Specification<T> thenSpec) {
            this.ifTrueSpec = thenSpec;
            return this;
        }

        public ComputationalIfSpecification<T> ellers(Specification<T> specification) {
            this.ifFalseSpec = specification;
            return this.build();
        }

        public ComputationalIfSpecification<T> build() {
            if (testSpec == null || ifTrueSpec == null) {
                throw new IllegalArgumentException("Mangler if/then");
            }
            return new ComputationalIfSpecification<>(testSpec, ifTrueSpec, ifFalseSpec);
        }
    }

    private Specification<T> testSpec;
    private Specification<T> ifTrueSpec;
    private Specification<T> ifFalseSpec;
    private Evaluation testEvaluation;
    private ServiceArgument property;


    public ComputationalIfSpecification(final Specification<T> testSpec, final Specification<T> doSpec) {
        this(testSpec, doSpec, null);
    }

    public ComputationalIfSpecification(final Specification<T> testSpec, final Specification<T> ifTrueSpec, final Specification<T> ifFalseSpec) {
        super();
        if (testSpec == null || ifTrueSpec == null) {
            throw new IllegalArgumentException("Mangler if/then");
        }
        this.testSpec = testSpec;
        this.ifTrueSpec = ifTrueSpec;
        this.ifFalseSpec = ifFalseSpec;
    }

    private Optional<Specification<T>> eller() {
        return Optional.ofNullable(ifFalseSpec);
    }

    @Override
    public String beskrivelse() {
        return beskrivelseIkkeTom()
            .orElseGet(() -> "(COMP HVIS/SÅ" + eller().map(s -> "/ELLERS").orElse("") + ")");
    }

    @Override
    public Evaluation evaluate(final T t) {
        testEvaluation = testSpec.evaluate(t);
        var conditionalEvaluation = Resultat.JA.equals(testEvaluation.result()) ? ifTrueSpec.evaluate(t) : doEvaluateIfFalse(t);
        var evaluation = new ComputationalIfEvaluation(identifikator(), beskrivelse(), testEvaluation, conditionalEvaluation);
        if (property != null) {
            evaluation.setEvaluationProperty(property.getBeskrivelse(), property.getVerdi().toString());
        }
        return evaluation;
    }

    private Evaluation doEvaluateIfFalse(final T t) {
        return ifFalseSpec != null ? ifFalseSpec.evaluate(t) : ja();
    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> "(HVIS " + testSpec.identifikator() + " SÅ " + ifTrueSpec.identifikator() + eller().map(s -> " ELLERS " + s.identifikator()).orElse("") + ")");
    }

    @Override
    public RuleDescription ruleDescription() {
        if (ifFalseSpec != null) {
            return new SpecificationRuleDescription(Operator.COMPUTATIONAL_IF, identifikator(), beskrivelse(), testSpec.ruleDescription(), ifTrueSpec.ruleDescription(), ifFalseSpec.ruleDescription());
        } else {
            return new SpecificationRuleDescription(Operator.COMPUTATIONAL_IF, identifikator(), beskrivelse(), testSpec.ruleDescription(), ifTrueSpec.ruleDescription());
        }
    }

    @Override
    public Specification<T> medEvaluationProperty(ServiceArgument property) {
        this.property = property;
        return this;
    }
}
