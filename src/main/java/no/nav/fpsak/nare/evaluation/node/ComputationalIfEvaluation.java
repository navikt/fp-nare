package no.nav.fpsak.nare.evaluation.node;

import java.util.Optional;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class ComputationalIfEvaluation extends AggregatedEvaluation {

    public ComputationalIfEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.COMPUTATIONAL_IF, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        boolean positiveResult = Resultat.JA.equals(firstChild().result());
        String testNodeNavn = Optional.ofNullable(firstChild().ruleIdentification()).orElse("betingelsen");
        String flowNodeNavn = Optional.ofNullable(secondChild().ruleIdentification()).orElse(positiveResult ? "flyt ved ja (andre barnenode)" : "flyt ved nei (tredje barnenode)");
        return "Utført " + testNodeNavn + (positiveResult ? " (ja)" : " (nei)") + " og " + flowNodeNavn;
    }

    @Override
    public Resultat result() {
        return secondChild().result();
    }

}
