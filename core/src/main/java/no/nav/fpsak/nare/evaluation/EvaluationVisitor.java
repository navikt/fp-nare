package no.nav.fpsak.nare.evaluation;

public interface EvaluationVisitor {
    boolean visiting(Operator operator, Evaluation parentEvaluation, Evaluation evaluation);
}
