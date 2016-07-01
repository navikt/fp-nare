package no.nav.aura.nare.specifications.common;

import no.nav.aura.nare.Resultat;

import java.io.Serializable;
import java.text.MessageFormat;

@SuppressWarnings("serial")
public class Evaluation implements Serializable {

    private Resultat result;
    private String reason;

    private Evaluation(Resultat result) {
        this.result = result;
    }

    public Evaluation(Resultat result, String reason, Object... stringformatArguments) {
        this(result);
        this.reason = MessageFormat.format(reason, stringformatArguments);
    }

    public Resultat result() {
        return result;
    }





    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "Evaluering: " + result + "\n\tBegrunnelse: " + reason + "";
    }

    public static final Evaluation yes(String reason, Object... stringformatArguments) {
        return new Evaluation(Resultat.INNVILGET, reason, stringformatArguments);
    }

    public static final Evaluation no(String reason, Object... stringformatArguments) {
        return new Evaluation(Resultat.AVSLAG, reason, stringformatArguments);
    }

    public static final Evaluation manual(String reason, Object... stringformatArguments) {
        return new Evaluation(Resultat.MANUELL_BEHANDLING, reason, stringformatArguments);
    }
}
