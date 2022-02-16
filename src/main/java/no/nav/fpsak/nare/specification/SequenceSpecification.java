package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.node.SequenceEvaluation;

/**
 * SEQUENCE specification, used to create a new specification that is a sequence of two other specifications,
 * ignoring the result of the first specification (ie assumed always JA)
 */
public class SequenceSpecification<T> extends AbstractSpecification<T> {

    private ServiceArgument scope;

    private final List<Specification<T>> specs = new ArrayList<>();

    public SequenceSpecification(final String id, final String beskrivelse, final List<Specification<T>> specs) {
        super();
        Objects.requireNonNull(id, "ID");
        Objects.requireNonNull(beskrivelse, "beskrivelse");
        Objects.requireNonNull(specs, "specs");
        if (specs.size() < 2) {
            throw new IllegalArgumentException("Utviklerfeil: SequenceSpecification må inneholde minst to Specifications.");
        }
        this.specs.addAll(specs);
        medID(id);
        medBeskrivelse(beskrivelse);
    }


    @SafeVarargs
    public SequenceSpecification(final String id, final String beskrivelse, final Specification<T>... specs) {
        this(id, beskrivelse, Arrays.asList(specs));
    }

    public SequenceSpecification(final String id, final String beskrivelse, final Specification<T> spec1, Specification<T> spec2) {
        this(id, beskrivelse, Arrays.asList(spec1, spec2));
    }

    private Specification<T> first() {
        return specs.get(0);
    }

    @Override
    public String beskrivelse() {
        if (super.beskrivelse().isEmpty()) {
            return "(" + first().beskrivelse() + " FULGT AV...)";
        }
        return super.beskrivelse();
    }

    @Override
    public Evaluation evaluate(final T t) {
        var evaluation = doEvaluate(t, null);
        if (scope != null) {
            evaluation.setEvaluationProperty(scope.getBeskrivelse(), scope.getVerdi().toString());
        }
        return evaluation;
    }

    @Override
    public Evaluation evaluate(final T t, ServiceArgument serviceArgument) {
        if (serviceArgument == null) {
            throw new IllegalArgumentException("Utviklerfeil: Førsøker evaluere Sequence med argument null");
        }
        var evaluation = doEvaluate(t, serviceArgument);
        if (scope != null) {
            evaluation.setEvaluationProperty(scope.getBeskrivelse(), scope.getVerdi().toString());
        }
        evaluation.setEvaluationProperty(serviceArgument.getBeskrivelse(), serviceArgument.getVerdi().toString());
        return evaluation;
    }

    private SequenceEvaluation doEvaluate(final T t, ServiceArgument serviceArgument) {
        var specSize = specs.size();
        Evaluation[] evaluations = new Evaluation[specSize];
        for (int ix = 0; ix < specSize; ix++) {
            var result = serviceArgument != null ? specs.get(ix).evaluate(t, serviceArgument) : specs.get(ix).evaluate(t);
            evaluations[ix] = result;
            if (ix < specSize - 1 && !Resultat.JA.equals(evaluations[ix].result())) {
                throw new IllegalArgumentException("Utviklerfeil: SequenceSpecification evaluering annet enn JA før siste spec.");
            }
        }
        return new SequenceEvaluation(identifikator(), beskrivelse(), evaluations);

    }

    @Override
    public String identifikator() {
        if (super.identifikator().isEmpty()) {
            return "(" + first().identifikator() + " FULGT AV...)";
        }
        return super.identifikator();
    }

    @Override
    public RuleDescription ruleDescription() {
        RuleDescription[] ruleDescriptions = new RuleDescription[specs.size()];
        for (int ix = 0; ix < specs.size(); ix++) {
            ruleDescriptions[ix] = specs.get(ix).ruleDescription();
        }
        return new SpecificationRuleDescription(Operator.SEQUENCE, identifikator(), beskrivelse(), ruleDescriptions);
    }

    @Override
    public Specification<T> medScope(ServiceArgument scope) {
        this.scope = scope;
        return this;
    }
}
