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
        int teller = 1;
        for (var child : children()) {
            if (teller != 1) {
                reasonText.append(OG);
            }
            String childId = child.ruleIdentification();
            reasonText.append(childId != null ? childId : "operasjon " + teller);
            teller++;
        }
        return reasonText.toString();
    }

    @Override
    public Resultat result() {
        return lastChild().result();
    }
}
