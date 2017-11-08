package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class SequenceEvaluation extends AggregatedEvaluation {

    public SequenceEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.SEQUENCE, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Utført " + first().ruleIdentification() + " og tilfredsstiller " + second().ruleIdentification();
        } else {
            return "Utført " + first().ruleIdentification() + " men tilfredsstiller ikke " + second().ruleIdentification();
        }

    }
    
    @Override
    public Resultat result() {
        return second().result();
    }

}
