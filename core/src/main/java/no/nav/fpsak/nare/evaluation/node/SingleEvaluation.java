package no.nav.fpsak.nare.evaluation.node;

import java.text.MessageFormat;
import java.util.Properties;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleReasonRef;

public class SingleEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Resultat resultat;
    private String reason;
    private RuleReasonRef outcomeReason;

    private Properties evaluationProperties;

    public SingleEvaluation(Resultat resultat, String ruleIdentification, String ruleDescription, RuleReasonRef outcome,
            Object... stringformatArguments) {
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.resultat = resultat;

        if (outcome != null) {
            this.outcomeReason = outcome;
            // TODO (FC): Lookup message text I18N
            this.reason = MessageFormat.format(outcomeReason.getReasonTextTemplate(), stringformatArguments);
        }
    }

    @Override
    public String reason() {
        return reason;
    }

    @Override
    public RuleReasonRef getOutcome() {
        return outcomeReason;
    }

    @Override
    public Resultat result() {
        return resultat;
    }

    @Override
    public String ruleDescription() {
        return ruleDescription;
    }

    @Override
    public String ruleIdentification() {
        return ruleIdentification;
    }

    public void setEvaluationProperties(Properties props) {
        this.evaluationProperties = props;
    }

    @Override
    public Properties getEvaluationProperties() {
        return evaluationProperties;
    }

    public void setEvaluationProperty(String key, int val) {
        if (this.evaluationProperties == null) {
            this.evaluationProperties = new Properties();
        }
        this.evaluationProperties.setProperty(key, String.valueOf(val));
    }
}
