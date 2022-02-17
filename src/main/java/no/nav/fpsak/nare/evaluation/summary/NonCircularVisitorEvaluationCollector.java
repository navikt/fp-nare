package no.nav.fpsak.nare.evaluation.summary;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.node.SingleEvaluation;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary.EvaluationVisitorCondition;

public class NonCircularVisitorEvaluationCollector implements EvaluationVisitor {

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
        if (Objects.equals(parentEvaluation, evaluation) &&
             (!(evaluation instanceof SingleEvaluation) || visited.contains(evaluation))) {
                // er ved roten. Special case med at roten er en Leaf
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