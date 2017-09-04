package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class OrEvaluation extends AggregatedEvaluation {

    public OrEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.OR, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller " + ruleOrIdentification();
        } else {
            return "Tilfredstiller hverken " + first().ruleIdentification() + " eller " + second().ruleIdentification();
        }
    }

    @Override
    public Resultat result() {
        return first().result().or(second().result());
    }

    private String ruleOrIdentification() {
        String firstID = first().result().equals(Resultat.JA) ? first().ruleIdentification() : "";
        String secondID = second().result().equals(Resultat.JA) ? second().ruleIdentification() : "";
        if (firstID.isEmpty())
            return secondID;
        if (secondID.isEmpty())
            return firstID;
        return firstID + " OG " + secondID;
    }

}
