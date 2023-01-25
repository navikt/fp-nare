package no.nav.fpsak.nare.evaluation.node;

import java.util.Optional;

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
            return "Tilfredstiller hverken " + navnFørsteNode() + " eller " + navnAndreNode();
        }
    }

    @Override
    public Resultat result() {
        return firstChild().result().or(secondChild().result());
    }

    private String ruleOrIdentification() {
        String firstID = firstChild().result().equals(Resultat.JA) ? navnFørsteNode() : "";
        String secondID = secondChild().result().equals(Resultat.JA) ? navnAndreNode() : "";
        if (firstID.isEmpty())
            return secondID;
        if (secondID.isEmpty())
            return firstID;
        return firstID + " OG " + secondID;
    }

    private String navnFørsteNode(){
        return Optional.ofNullable(firstChild().ruleIdentification()).orElse("første betingelse");
    }

    private String navnAndreNode(){
        return Optional.ofNullable(secondChild().ruleIdentification()).orElse("andre betingelse");
    }

}
