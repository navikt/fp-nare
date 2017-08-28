package no.nav.aura.nare.evaluation;

import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.text.MessageFormat;

public class SingleEvaluation implements Evaluation {

    private String ruleIdentification;
    private String ruleDescription;
    private Resultat resultat;
    private String reason;

    public SingleEvaluation(Resultat resultat, String ruleIdentification, String ruleDescription, String reason, Object... stringformatArguments) {
        this.ruleIdentification = ruleIdentification;
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
        return ruleIdentification;
    }

    @Override
    public String reason() {
        return reason;
    }


}
