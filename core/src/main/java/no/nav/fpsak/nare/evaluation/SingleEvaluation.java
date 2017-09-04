package no.nav.fpsak.nare.evaluation;

import java.text.MessageFormat;

public class SingleEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Resultat resultat;
    private String reason;
    private String reasonKey;
    private String reasonCode;

    public SingleEvaluation(Resultat resultat, String ruleIdentification, String ruleDescription, String reasonCode, String reasonKey,
            Object... stringformatArguments) {
        this.ruleIdentification = ruleIdentification;
        this.ruleDescription = ruleDescription;
        this.resultat = resultat;
        this.reasonKey = reasonKey;
        this.reasonCode = reasonCode;
        // TODO (FC): Lookup message text I18N
        this.reason = MessageFormat.format(reasonKey, stringformatArguments);
    }

    @Override
    public String reason() {
        return reason;
    }

    @Override
    public String reasonCode() {
        return reasonCode;
    }

    public String reasonKey() {
        return reasonKey;
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
