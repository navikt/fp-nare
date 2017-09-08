package no.nav.fpsak.nare.evaluation;

public interface Evaluation {

    RuleReasonRef getOutcome();
    
    String reason();
    
    Resultat result();

    String ruleDescription();

    String ruleIdentification();

    default void visit(Evaluation parent, EvaluationVisitor visitor) {
        visitor.visiting(null, parent, this); // NOSONAR
    }



}
