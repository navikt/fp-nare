package no.nav.fpsak.nare.evaluation.node;

import no.nav.fpsak.nare.evaluation.AggregatedEvaluation;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;
import no.nav.fpsak.nare.evaluation.Resultat;

public class ForeachEvaluation extends AggregatedEvaluation {

    private String argName;

    public ForeachEvaluation(String id, String ruleDescription, String argName, Evaluation... children) {
        super(Operator.FOREACH, id, ruleDescription, children);
        this.argName = argName;
    }

    @Override
    public String reason() {
        return "Utført id for alle " + argName;
    }
    
    @Override
    public Resultat result() {
        // Endre hvis man tillater andre outcomes fra loop
        return Resultat.JA;
    }
}
