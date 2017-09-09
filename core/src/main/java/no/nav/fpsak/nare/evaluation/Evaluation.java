package no.nav.fpsak.nare.evaluation;

import java.util.Properties;

import no.nav.fpsak.nare.evaluation.summary.EvaluationVisitor;

public interface Evaluation {

    RuleReasonRef getOutcome();

    String reason();

    Resultat result();

    String ruleDescription();

    String ruleIdentification();

    default void visit(Evaluation parent, EvaluationVisitor visitor) {
        visitor.visiting(null, parent, this); // NOSONAR
    }

    /** Properties generert som del av evaluering. Kan brukes til å angi custom resultater. */
    default Properties getEvaluationProperties() {
        return null;
    }

}
