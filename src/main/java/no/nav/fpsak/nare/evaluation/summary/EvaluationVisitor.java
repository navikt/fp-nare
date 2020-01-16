package no.nav.fpsak.nare.evaluation.summary;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Operator;

public interface EvaluationVisitor {
    boolean visiting(Operator operator, Evaluation parentEvaluation, Evaluation evaluation);
}
