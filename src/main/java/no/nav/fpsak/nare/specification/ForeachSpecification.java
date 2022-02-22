package no.nav.fpsak.nare.specification;

import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.ForeachEvaluation;

/**
 * FOREACH specification, evaluate a specification for each member of a Collection, setting medScope to the current member
 */
public class ForeachSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec;
    private List<?> args;
    private String argName;

    public ForeachSpecification(Specification<T> spec, List<?> args, String argName) {
        super();
        Objects.requireNonNull(spec, "spec");
        Objects.requireNonNull(args, "args");
        Objects.requireNonNull(argName, "argName");
        this.spec = spec;
        this.args = args;
        this.argName = argName;
    }

    public ForeachSpecification(String id, String beskrivelse, Specification<T> spec, List<?> args, String argName) {
        this(spec, args, argName);
        Objects.requireNonNull(id, "ID");
        Objects.requireNonNull(beskrivelse, "beskrivelse");
        medID(id);
        medBeskrivelse(beskrivelse);
    }

    @Override
    public String beskrivelse() {
        return beskrivelseIkkeTom().orElseGet(() -> "(FOREACH " + argName + ")");
    }

    @Override
    public Evaluation evaluate(final T t) {
        int ix = 0;
        Evaluation[] evaluations = new Evaluation[args.size()];
        for (var arg : args) {
            var serviceArgument = new ServiceArgument(argName, arg);
            evaluations[ix] = spec.evaluate(t, serviceArgument);
            ix++;
        }
        var resultingEval = new ForeachEvaluation(identifikator(), beskrivelse(), evaluations);
        return resultingEval;
    }

    @Override
    public Evaluation evaluate(final T t, ServiceArgument serviceArgument) {
        if (serviceArgument != null) {
            throw new IllegalArgumentException("Utviklerfeil: Støtter ikke (nøstet) FOREACH med argumenter");
        }
        return evaluate(t);
    }

    @Override
    public String identifikator() {
        return identifikatorIkkeTom().orElseGet(() -> "(FOREACH " + argName + ": " + spec.identifikator() + ")");
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.FOREACH, identifikator(), beskrivelse(), spec.ruleDescription());
    }
}
