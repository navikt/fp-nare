package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.SequenceEvaluation;

/**
 * SEQUENCE specification, used to create a new specification that is a sequence of two other specifications,
 * ignoring the result of the first specification (ie assumed always JA)
 */
public class SequenceSpecification<T> extends AbstractSpecification<T> {

    public static <V> SequenceSpecification.Builder<V> regel() {
        return new Builder<>();
    }

    public static <V> SequenceSpecification.Builder<V> regel(String id, String beskrivelse) {
        return new Builder<>(id, beskrivelse);
    }

    public static class Builder<T> {
        private final List<Specification<T>> sekvens = new ArrayList<>();
        private String id;
        private String beskrivelse;

        public Builder() {
        }

        public Builder(String id, String beskrivelse) {
            this.id = id;
            this.beskrivelse = beskrivelse;
        }

        public Builder<T> neste(Specification<T> specification) {
            this.sekvens.add(specification);
            return this;
        }

        public Builder<T> hvis(Specification<T> test, Specification<T> spec) {
            this.sekvens.add(new ComputationalIfSpecification<>(test, spec));
            return this;
        }

        public Builder<T> hvisEllers(Specification<T> test, Specification<T> hvis, Specification<T> eller) {
            this.sekvens.add(new ComputationalIfSpecification<>(test, hvis, eller));
            return this;
        }

        public Builder<T> forAlle(String argName, List<?> args, Specification<T> spec) {
            this.sekvens.add(new ForeachSpecification<>(spec, args, argName));
            return this;
        }

        public SequenceSpecification<T> siste(Specification<T> specification) {
            this.sekvens.add(specification);
            return build();
        }

        public SequenceSpecification<T> build() {
            var spec = new SequenceSpecification<T>(sekvens);
            if (id != null) {
                spec.medID(id).medBeskrivelse(beskrivelse);
            }
            return spec;
        }
    }

    private ServiceArgument property;

    private final List<Specification<T>> specs = new ArrayList<>();

    public SequenceSpecification(final List<Specification<T>> specs) {
        super();
        Objects.requireNonNull(specs, "specs");
        if (specs.size() < 2) {
            throw new IllegalArgumentException("Utviklerfeil: SequenceSpecification må inneholde minst to Specifications.");
        }
        this.specs.addAll(specs);
    }

    public SequenceSpecification(final String id, final String beskrivelse, final List<Specification<T>> specs) {
        this(specs);
        Objects.requireNonNull(id, "ID");
        Objects.requireNonNull(beskrivelse, "beskrivelse");
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

    private Specification<T> last() {
        return specs.get(specs.size()-1);
    }

    @Override
    public String beskrivelse() {
        return beskrivelseIkkeTom().orElse("(SEKVENS ...)");
    }

    @Override
    public Evaluation evaluate(final T t) {
        var evaluation = doEvaluate(t, null);
        if (property != null) {
            evaluation.setEvaluationProperty(property.getBeskrivelse(), property.getVerdi().toString());
        }
        return evaluation;
    }

    @Override
    public Evaluation evaluate(final T t, ServiceArgument serviceArgument) {
        if (serviceArgument == null) {
            throw new IllegalArgumentException("Utviklerfeil: Førsøker evaluere Sequence med argument null");
        }
        var evaluation = doEvaluate(t, serviceArgument);
        if (property != null) {
            evaluation.setEvaluationProperty(property.getBeskrivelse(), property.getVerdi().toString());
        }
        evaluation.setEvaluationProperty(serviceArgument.getBeskrivelse(), serviceArgument.getVerdi().toString());
        return evaluation;
    }

    private SequenceEvaluation doEvaluate(final T t, ServiceArgument serviceArgument) {
        var specSize = specs.size();
        Evaluation[] evaluations = new Evaluation[specSize];
        for (int ix = 0; ix < specSize; ix++) {
            evaluations[ix] = serviceArgument != null ? specs.get(ix).evaluate(t, serviceArgument) : specs.get(ix).evaluate(t);
        }
        return new SequenceEvaluation(identifikator(), beskrivelse(), evaluations);

    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> "(SEKVENS " + first().identifikator() + " ... " + last().identifikator() + ")");
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
        this.property = scope;
        return this;
    }

    @Override
    public Specification<T> medEvaluationProperty(ServiceArgument property) {
        this.property = property;
        return this;
    }
}
