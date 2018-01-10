package no.nav.fpsak.nare.evaluation.summary;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

/** Hjelpeklasse for å lage sammendrag av en evaluering. */
public class EvaluationSummary {

    interface EvaluationVisitorCondition {
        boolean check(Operator operator, Evaluation parent, Evaluation child);
    }

    private Evaluation rootEvaluation;

    public EvaluationSummary(Evaluation rootEvaluation) {
        this.rootEvaluation = rootEvaluation;
    }

    public Collection<Evaluation> leafEvaluations(Resultat... acceptedResults) {
        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
                : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(
                (op, parent, child) -> op == null && accepted.contains(child.result()));
        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected();
    }

    public Collection<String> leafReasons(Resultat... acceptedResults) {

        Set<Resultat> accepted = acceptedResults.length > 0 ? EnumSet.copyOf(Arrays.asList(acceptedResults))
                : EnumSet.allOf(Resultat.class);

        NonCircularVisitorEvaluationCollector visitor = new NonCircularVisitorEvaluationCollector(
                new EvaluationVisitorCondition() {
                    @Override
                    public boolean check(Operator op, Evaluation parent, Evaluation child) {
                        return Operator.SINGLE.equals(op) && accepted.contains(child.result());
                    }
                });

        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected().stream()
                .filter(e -> e.getOutcome() != null)
                .map(this::getReasonCode).distinct().collect(Collectors.toList());
    }

    private String getReasonCode(Evaluation e) {
        return e.getOutcome() == null ? null : e.getOutcome().getReasonCode();
    }
}
