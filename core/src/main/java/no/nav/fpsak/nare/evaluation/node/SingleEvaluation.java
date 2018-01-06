package no.nav.fpsak.nare.evaluation.node;

import java.text.MessageFormat;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.BasicEvaluation;
import no.nav.fpsak.nare.evaluation.EvaluationRuleDescription;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;

public class SingleEvaluation extends BasicEvaluation {

    private RuleReasonRef outcomeReason;

    public SingleEvaluation(Resultat resultat, String ruleIdentification, String ruleDescription, RuleReasonRef outcome,
            Object... stringformatArguments) {
        super(ruleIdentification, ruleDescription);

        super.resultat = resultat;
        super.reason = outcome == null ? null : MessageFormat.format(outcome.getReasonTextTemplate(), stringformatArguments);
        this.outcomeReason = outcome;
    }

    @Override
    public RuleReasonRef getOutcome() {
        return outcomeReason;
    }

    @Override
    public RuleDescription toRuleDescription() {
        return new SingleEvaluationRuleDescription(this);
    }

    static class SingleEvaluationRuleDescription extends EvaluationRuleDescription {
        @SuppressWarnings("unused")
        private final RuleReasonRef outcomeReason;

        public SingleEvaluationRuleDescription(SingleEvaluation evaluation) {
            super(Operator.SINGLE, evaluation);
            outcomeReason = evaluation.getOutcome();
        }
    }

}
