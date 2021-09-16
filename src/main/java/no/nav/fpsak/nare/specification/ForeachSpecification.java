package no.nav.fpsak.nare.specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.node.ForeachEvaluation;

/**
 * FOREACH specification, evaluate a specification for each member of a Collection, setting medScope to the current member
 */
public class ForeachSpecification<T> extends AbstractSpecification<T> {

    private Specification<T> spec;
    private Collection<?> args;
    private String argName;

    public ForeachSpecification(String id, String beskrivelse, Specification<T> spec, Collection<?> args, String argName) {
        super();
        Objects.requireNonNull(id, "ID");
        Objects.requireNonNull(beskrivelse, "beskrivelse");
        Objects.requireNonNull(spec, "spec");
        Objects.requireNonNull(args, "args");
        Objects.requireNonNull(argName, "argName");
        this.spec = spec;
        this.args = args;
        this.argName = argName;
        medID(id);
        medBeskrivelse(beskrivelse);
    }

    @Override
    public String beskrivelse() {
        if (super.beskrivelse().isEmpty()) {
            return "(FOREACH " + argName + ": " + spec.beskrivelse() + ")";
        }
        return super.beskrivelse();
    }

    @Override
    public Evaluation evaluate(final T t) {
        return evaluate(t, List.of());
    }

    @Override
    public Evaluation evaluate(final T t, List<ServiceArgument> serviceArguments) {
        int ix = 0;
        var useForNextIteration = t;
        Evaluation[] evaluations = new Evaluation[args.size()];
        for (var arg : args) {
            var extendedArguments = new ArrayList<>(serviceArguments);
            var serviceArgument = new ServiceArgument(argName, arg);
            extendedArguments.add(serviceArgument);
            var eval = spec.medScope(serviceArgument).evaluate(useForNextIteration, extendedArguments);
            evaluations[ix] = eval;
            if (eval.output() != null) {
                useForNextIteration = eval.output();
            }
            if (!Resultat.JA.equals(evaluations[ix].result())) {
                throw new IllegalArgumentException("Utviklerfeil: ForeachSpecification evaluering annet enn JA.");
            }
            ix++;
        }
        var resultingEval = new ForeachEvaluation(identifikator(), beskrivelse(), argName, evaluations);
        if (!Objects.equals(t, useForNextIteration)) {
            resultingEval.setOutput(useForNextIteration);
        }
        return resultingEval;
    }

    @Override
    public String identifikator() {
        if (super.identifikator().isEmpty()) {
            return "(FOREACH " + argName + ": " + spec.beskrivelse() + ")";
        }
        return super.identifikator();
    }

    @Override
    public RuleDescription ruleDescription() {
        return new SpecificationRuleDescription(Operator.FOREACH, identifikator(), beskrivelse(), spec.ruleDescription());
    }
}
