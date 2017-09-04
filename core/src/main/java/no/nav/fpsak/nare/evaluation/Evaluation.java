package no.nav.fpsak.nare.evaluation;

public interface Evaluation {

    String reason();

    // TODO: Splitt AggregatedEvaluation og SingleEvaluation tydligere? Leaf og Gates?
    default String reasonCode() {
        return null;
    }

    Resultat result();

    String ruleDescription();

    String ruleIdentification();

    default void visit(Evaluation parent, EvaluationVisitor visitor) {
        visitor.visiting(null, parent, this); // NOSONAR
    }

}
