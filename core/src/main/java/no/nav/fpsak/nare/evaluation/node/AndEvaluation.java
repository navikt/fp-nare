package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;
import no.nav.fpsak.nare.evaluation.Resultat;

public class AndEvaluation extends AggregatedEvaluation {

    public AndEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.AND, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller både " + first().ruleIdentification() + " og " + second().ruleIdentification();
        } else {
            return "Tilfredstiller ikke både " + first().ruleIdentification() + " og " + second().ruleIdentification();
        }

    }
    
    @Override
    public Resultat result() {
        return first().result().and(second().result());
    }

}
