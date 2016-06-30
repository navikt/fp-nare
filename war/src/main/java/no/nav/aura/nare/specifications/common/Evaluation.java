package no.nav.aura.nare.specifications.common;

import java.io.Serializable;
import java.text.MessageFormat;

@SuppressWarnings("serial")
public class Evaluation implements Serializable {

    private boolean satisfied;
    private String reason;

    private Evaluation(boolean satisfied) {
        this.satisfied = satisfied;
    }

    private Evaluation(boolean satisfied, String reason, Object... stringformatArguments) {
        this(satisfied);
        this.reason = MessageFormat.format(reason, stringformatArguments);
    }

    public boolean isSatisfied() {
        return satisfied;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        return "Evaluering: " + satisfied + "\n\tBegrunnelse: " + reason + "";
    }

    public static final Evaluation yes(String reason, Object... stringformatArguments) {
        return new Evaluation(true, reason, stringformatArguments);
    }

    public static final Evaluation no(String reason, Object... stringformatArguments) {
        return new Evaluation(false, reason, stringformatArguments);
    }
}
