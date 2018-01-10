package no.nav.fpsak.nare.evaluation;

import java.util.Map;

import no.nav.fpsak.nare.doc.RuleDescription;
import no.nav.fpsak.nare.evaluation.summary.EvaluationVisitor;

public interface Evaluation {

    RuleReasonRef getOutcome();

    String reason();

    Resultat result();

    String ruleDescriptionText();

    String ruleIdentification();

    void visit(Evaluation parent, EvaluationVisitor visitor);

    /** Properties generert som del av evaluering. Kan brukes til å angi custom resultater. */
    Map<String, Object> getEvaluationProperties();

    void setEvaluationProperty(String key, Object value);

    /** Utled en RuleDescription som beskriver denne noden i Evalueringen. */
    RuleDescription toRuleDescription();

}
