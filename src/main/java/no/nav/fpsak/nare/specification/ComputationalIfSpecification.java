package no.nav.fpsak.nare.specification;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.node.ComputationalIfEvaluation;

/**
 * Computational IF specification, used to create a new specification that is a choice between two other specifications.
 *
 * Diskuter: extende Abstract siden denne ikke er binær,
 * beskrivelse+identifikator = "HVIS .... SÅ ... ELLERS ..."
 * behov for medScope - den setter kun en property som gir sporingsoutput ifm DynRuleService - ingen annen bruk
 * utvide med "uten else" - spec2 == null -> returnere ja(passende ID)
 */
public class ComputationalIfSpecification<T> extends BinarySpecification<T> {

    private Specification<T> testSpec;
    private Evaluation testEvaluation;
    private ServiceArgument scope;

    public ComputationalIfSpecification(final Specification<T> testSpec, final Specification<T> spec1, final Specification<T> spec2) {
        super(spec1, spec2);
        this.testSpec = testSpec;
    }

    @Override
    public String beskrivelse() {
        return "...";
    }

    @Override
    public Evaluation evaluate(final T t) {
        testEvaluation = testSpec.evaluate(t);
        var conditionalEvaluation = Resultat.JA.equals(testEvaluation.result()) ? spec1.evaluate(t) : spec2.evaluate(t);
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
        var conditionalEvaluation = Resultat.JA.equals(testEvaluation.result()) ? spec1.evaluate(t, serviceArgument) : spec2.evaluate(t, serviceArgument);
        var evaluation = new ComputationalIfEvaluation(identifikator(), beskrivelse(), testEvaluation, conditionalEvaluation);
        if (scope != null) {
            evaluation.setEvaluationProperty(scope.getBeskrivelse(), scope.getVerdi().toString());
        }
        evaluation.setEvaluationProperty(serviceArgument.getBeskrivelse(), serviceArgument.getVerdi().toString());
        return evaluation;
    }

    @Override
    public String identifikator() {
        return "...";
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.COMPUTATIONAL_IF, identifikator(), beskrivelse(), testSpec.ruleDescription(), spec1.ruleDescription(), spec2.ruleDescription());
    }

    @Override
    public Specification<T> medScope(ServiceArgument scope) {
        this.scope = scope;
        return this;
    }
}
