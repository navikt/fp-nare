package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.summary.EvaluationVisitor;

public class ConditionalOrEvaluation extends AggregatedEvaluation {

    public ConditionalOrEvaluation(String id, String ruleDescription, Evaluation testEval, Evaluation flowEval) {
        super(Operator.COND_OR, id, ruleDescription, testEval, flowEval);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller flyt (test=" + firstChild().ruleIdentification() + ")/" + secondChild().ruleIdentification();
        } else {
            return "Tilfredstiller ikke flyt (test=" + firstChild().ruleIdentification() + ")/" + secondChild().ruleIdentification();
        }

    }

    /** Skipper testEvals, kun flowEval */
    @Override
    protected void visitChildren(EvaluationVisitor visitor) {
        secondChild().visit(this, visitor);
    }

    @Override
    public Resultat result() {
        return firstChild().result().and(secondChild().result());
    }

}
