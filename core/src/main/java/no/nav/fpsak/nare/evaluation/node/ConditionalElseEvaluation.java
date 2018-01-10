package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class ConditionalElseEvaluation extends AggregatedEvaluation {
    
    public ConditionalElseEvaluation(String id, String ruleDescription, Evaluation elseEvaluation) {
        super(Operator.COND_OR, id, ruleDescription, elseEvaluation);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller flyt (*/" + firstChild().ruleIdentification();
        } else {
            // er dette mulig?
            return "Tilfredstiller ikke flyt (*/" + firstChild().ruleIdentification();
        }

    }

    @Override
    public Resultat result() {
        return firstChild().result();
    }

}
