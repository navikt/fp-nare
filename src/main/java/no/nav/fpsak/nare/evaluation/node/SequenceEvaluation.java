package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class SequenceEvaluation extends AggregatedEvaluation {

    private static final String OG = " og ";

    public SequenceEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.SEQUENCE, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        var reasonText = new StringBuilder("Utført ");
        for (var child : children()) {
            reasonText.append(child.ruleIdentification()).append(OG);
        }
        var finalOg = reasonText.lastIndexOf(OG);
        reasonText.delete(finalOg, finalOg + OG.length());
        return reasonText.toString();
    }
    
    @Override
    public Resultat result() {
        return lastChild().result();
    }
}
