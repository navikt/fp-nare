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

    public static <V> Builder<V> regel() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private Specification<T> testSpec;
        private Specification<T> ifTrueSpec;
        private Specification<T> ifFalseSpec;

        public Builder() {
        }

        public Builder<T> hvis(Specification<T> ifSpec, Specification<T> thenSpec) {
            this.testSpec = ifSpec;
            this.ifTrueSpec = thenSpec;
            return this;
        }

        public  ComputationalIfSpecification<T> hvisEllersJa(Specification<T> ifSpec, Specification<T> thenSpec) {
            this.testSpec = ifSpec;
            this.ifTrueSpec = thenSpec;
            return this.build();
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
    private ServiceArgument scope;


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
        var conditionalEvaluation = Resultat.JA.equals(testEvaluation.result()) ? ifTrueSpec.evaluate(t) : doEvaluateIfFalse(t, null);
        var evaluation = new ComputationalIfEvaluation(identifikator(), beskrivelse(), testEvaluation, conditionalEvaluation);
        if (scope != null) {
            evaluation.setEvaluationProperty(scope.getBeskrivelse(), scope.getVerdi().toString());
        }
        return evaluation;
    }

    @Override
    public Evaluation evaluate(final T t, ServiceArgument serviceArgument) {
        if (serviceArgument == null) {
            throw new IllegalArgumentException("Utviklerfeil: Førsøker evaluere ComputationalIf med argument null");
        }
        testEvaluation = testSpec.evaluate(t, serviceArgument);
        var conditionalEvaluation = Resultat.JA.equals(testEvaluation.result()) ? ifTrueSpec.evaluate(t, serviceArgument) : doEvaluateIfFalse(t, serviceArgument);
        var evaluation = new ComputationalIfEvaluation(identifikator(), beskrivelse(), testEvaluation, conditionalEvaluation);
        if (scope != null) {
            evaluation.setEvaluationProperty(scope.getBeskrivelse(), scope.getVerdi().toString());
        }
        evaluation.setEvaluationProperty(serviceArgument.getBeskrivelse(), serviceArgument.getVerdi().toString());
        return evaluation;
    }

    private Evaluation doEvaluateIfFalse(final T t, ServiceArgument serviceArgument) {
        if (ifFalseSpec != null) {
            return serviceArgument != null ? ifFalseSpec.evaluate(t, serviceArgument) : ifFalseSpec.evaluate(t);
        } else {
            return ja();
        }
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
    public Specification<T> medScope(ServiceArgument scope) {
        this.scope = scope;
        return this;
    }
}
