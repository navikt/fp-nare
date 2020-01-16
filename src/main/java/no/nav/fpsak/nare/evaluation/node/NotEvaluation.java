package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class NotEvaluation extends AggregatedEvaluation {

    public NotEvaluation(String id, String ruleDescription, Evaluation child) {
        super(Operator.NOT, id, ruleDescription, child);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller det motsatte av " + firstChild().ruleIdentification();
        } else {
            return "Tilfredstiller ikke det motsatte av " + firstChild().ruleIdentification();
        }

    }

    @Override
    public Resultat result() {
        return firstChild().result().not();
    }

}
