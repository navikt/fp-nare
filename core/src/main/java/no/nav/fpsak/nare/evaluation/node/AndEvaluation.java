package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class AndEvaluation extends AggregatedEvaluation {

    public AndEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.AND, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller både " + firstChild().ruleIdentification() + " og " + secondChild().ruleIdentification();
        } else {
            return "Tilfredstiller ikke både " + firstChild().ruleIdentification() + " og " + secondChild().ruleIdentification();
        }

    }
    
    @Override
    public Resultat result() {
        return firstChild().result().and(secondChild().result());
    }

}
