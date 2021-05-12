package no.nav.fpsak.nare.evaluation;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import no.nav.fpsak.nare.doc.BasicRuleDescription;

/**
 * Beskriver en Evaluation som en RuleDescription mhp. dokumenasjon av kjøring.
 * Serialiseres til JSON.
 */
public class EvaluationRuleDescription extends BasicRuleDescription {

    @SuppressWarnings("unused")
    private final Resultat resultat;
    @SuppressWarnings("unused")
    private final String reason;
    @SuppressWarnings("unused")
    private final Map<String, Object> evaluationProperties;

    public EvaluationRuleDescription(Operator operator, Evaluation evaluation) {
        this(operator, evaluation, Collections.emptyList());
    }

    public EvaluationRuleDescription(Operator operator, Evaluation evaluation,
            List<? extends Evaluation> children) {
        super(operator, evaluation.ruleIdentification(), evaluation.ruleDescriptionText(),
                children == null ? Collections.emptyList()
                        : children.stream().map(Evaluation::toRuleDescription).collect(Collectors.toList()));
        this.resultat = evaluation.result();
        this.reason = evaluation.reason();
        this.evaluationProperties = evaluation.getEvaluationProperties();
    }

}