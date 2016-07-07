package no.nav.aura.nare.evalulation;

import no.nav.aura.nare.evaluering.Resultat;

import java.text.MessageFormat;


public class SingleEvaluation implements Evaluation {

    private String ruleIdentifcation;
    private String ruleDescription;
    private Resultat resultat;
    private String reason;

    public SingleEvaluation(Resultat resultat, String ruleIdentifcation, String ruleDescription, String reason, Object... stringformatArguments) {
        this.ruleIdentifcation = ruleIdentifcation;
        this.ruleDescription = ruleDescription;
        this.resultat = resultat;
        this.reason = MessageFormat.format(reason, stringformatArguments);
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
        return ruleIdentifcation;
    }

    @Override
    public String reason() {
        return reason;
    }
}
