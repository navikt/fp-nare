package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class ForeachEvaluation extends AggregatedEvaluation {

    public ForeachEvaluation(String id, String ruleDescription, Evaluation... children) {
        super(Operator.FOREACH, id, ruleDescription, children);
    }

    @Override
    public String reason() {
        return "Utført " + lastChild().ruleIdentification();
    }
    
    @Override
    public Resultat result() {
        // Endre hvis man tillater andre outcomes fra loop
        return lastChild().result();
    }
}
