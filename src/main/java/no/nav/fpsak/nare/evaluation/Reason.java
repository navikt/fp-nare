package no.nav.fpsak.nare.evaluation;

import java.text.MessageFormat;

public class Reason {

    private final String code;
    private final String text;

    public Reason(String code, String textKey, Object... textArgs) {
        this.code = code;
        this.text = MessageFormat.format(textKey, textArgs);
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
