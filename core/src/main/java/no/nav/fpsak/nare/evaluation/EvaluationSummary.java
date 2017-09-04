package no.nav.fpsak.nare.evaluation;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** Hjelpeklasse for å lage sammendrag av en evaluering. */
public class EvaluationSummary {

    interface EvaluationVisitorCondition {
        boolean check(Operator operator, Evaluation parent, Evaluation child);
    }

    class NonCircularVisitorEvaluationCollector implements EvaluationVisitor {

        private final Set<Evaluation> visited = new LinkedHashSet<>();
        private final Set<Evaluation> collected = new LinkedHashSet<>();
        private EvaluationVisitorCondition condition;

        public NonCircularVisitorEvaluationCollector(EvaluationVisitorCondition condition) {
            this.condition = condition;
        }

        public Set<Evaluation> getCollected() {
            return collected;
        }

        @Override
        public boolean visiting(Operator operator, Evaluation parentEvaluation, Evaluation evaluation) {
            if (Objects.equals(parentEvaluation, evaluation)) {
                // er ved roten
                return true;
            }
            boolean added = visited.add(evaluation);
            if (!added) {
                return false;
            }
            doVisit(operator, parentEvaluation, evaluation);
            return true;
        }

        protected void doVisit(Operator operator, Evaluation parentEvaluation, Evaluation evaluation) {
            if (condition.check(operator, parentEvaluation, evaluation)) {
                collected.add(evaluation);
            }
        }

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
                (op, parent, child) -> op == null && accepted.contains(child.result()));

        rootEvaluation.visit(rootEvaluation, visitor);
        return visitor.getCollected().stream().map(e -> e.reasonCode()).distinct().collect(Collectors.toList());
    }
}
