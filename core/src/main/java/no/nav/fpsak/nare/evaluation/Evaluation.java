package no.nav.fpsak.nare.evaluation;

public interface Evaluation {

    DetailReasonKey getOutcome();
    
    String reason();
    
    Resultat result();

    String ruleDescription();

    String ruleIdentification();

    default void visit(Evaluation parent, EvaluationVisitor visitor) {
        visitor.visiting(null, parent, this); // NOSONAR
    }



}
