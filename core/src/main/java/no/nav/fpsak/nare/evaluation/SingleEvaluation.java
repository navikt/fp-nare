package no.nav.fpsak.nare.evaluation;

import java.text.MessageFormat;

public class SingleEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Resultat resultat;
    private String reason;
    private DetailReasonKey outcomeReason;

    public SingleEvaluation(Resultat resultat, String ruleIdentification, String ruleDescription, DetailReasonKey outcome,
            Object... stringformatArguments) {
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.resultat = resultat;
        this.outcomeReason = outcome;
        // TODO (FC): Lookup message text I18N
        this.reason = MessageFormat.format(outcomeReason.getReasonTextTemplate(), stringformatArguments);
    }

    @Override
    public String reason() {
        return reason;
    }
    
    @Override
    public DetailReasonKey getOutcome() {
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
}
