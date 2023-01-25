package no.nav.fpsak.nare.evaluation.node;

import java.util.Optional;

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
        String node1navn = Optional.ofNullable(firstChild().ruleIdentification()).orElse("første betingelse");
        String node2Navn = Optional.ofNullable(secondChild().ruleIdentification()).orElse("andre betingelse");
        if (result().equals(Resultat.JA)) {
            return "Tilfredstiller både " + node1navn + " og " + node2Navn;
        } else {
            return "Tilfredstiller ikke både " + node1navn + " og " + node2Navn;
        }

    }

    @Override
    public Resultat result() {
        return firstChild().result().and(secondChild().result());
    }

}
