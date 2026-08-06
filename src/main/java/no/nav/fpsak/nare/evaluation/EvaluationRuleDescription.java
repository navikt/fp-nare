package no.nav.fpsak.nare.evaluation;

import no.nav.fpsak.nare.doc.BasicRuleDescription;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Beskriver en Evaluation som en RuleDescription mhp. dokumenasjon av kjøring.
 * Serialiseres til JSON.
 */
public class EvaluationRuleDescription extends BasicRuleDescription {

    private final Resultat resultat;
    @SuppressWarnings("unused")
    private final String reason;
    private final Map<String, Object> evaluationProperties;

    public EvaluationRuleDescription(Operator operator, Evaluation evaluation) {
        this(operator, evaluation, List.of());
    }

    public EvaluationRuleDescription(Operator operator, Evaluation evaluation,
            List<? extends Evaluation> children) {
        super(operator, evaluation.ruleIdentification(), evaluation.ruleDescriptionText(),
                children == null ? List.of() : children.stream().map(Evaluation::toRuleDescription).toList());
        this.resultat = evaluation.result();
        this.reason = evaluation.reason();
        this.evaluationProperties = evaluation.getEvaluationProperties();
    }

    public EvaluationRuleDescription(Operator operator, String ruleId, String ruleDescription, Resultat resultat, String reason, Map<String, Object> evaluationProperties) {
        super(operator, ruleId, ruleDescription);
        this.resultat = resultat;
        this.reason = reason;
        this.evaluationProperties = evaluationProperties;
    }

    public Resultat getResultat() {
        return resultat;
    }

    public Map<String, Object> getEvaluationProperties() {
        return evaluationProperties;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof EvaluationRuleDescription that)) return false;
        if (!super.equals(o)) return false;

        return getResultat() == that.getResultat() && Objects.equals(reason, that.reason)
                && Objects.equals(getEvaluationProperties(), that.getEvaluationProperties());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getResultat());
        result = 31 * result + Objects.hashCode(reason);
        result = 31 * result + Objects.hashCode(getEvaluationProperties());
        return result;
    }
}