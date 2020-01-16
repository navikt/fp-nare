package no.nav.fpsak.nare.evaluation.node;

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
        return "Utført " + firstChild().ruleIdentification() + (Resultat.JA.equals(firstChild().result()) ? " (ja)" : " (nei)") + " og " + secondChild().ruleIdentification();
    }
    
    @Override
    public Resultat result() {
        return secondChild().result();
    }

}
