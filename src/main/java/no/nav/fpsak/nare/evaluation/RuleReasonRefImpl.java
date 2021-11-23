package no.nav.fpsak.nare.evaluation;

/*
 * Bruk RuleOutcome eller implementer en lokal RuleReasonRef som kan gi ut en enum eller record, unngå kode-magi
 */
@Deprecated(forRemoval = true)
public class RuleReasonRefImpl implements RuleReasonRef {

    private final String reasonCode;
    private final String reason;

    public RuleReasonRefImpl(String reasonCode, String reason) {
        this.reasonCode = reasonCode;
        this.reason = reason;
    }

    @Override
    public String getReasonTextTemplate() {
        return reason;
    }

    @Override
    public String getReasonCode() {
        return reasonCode;
    }

}
