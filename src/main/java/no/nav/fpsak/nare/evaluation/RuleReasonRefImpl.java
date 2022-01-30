package no.nav.fpsak.nare.evaluation;

/*
 * Bruk RuleOutcome eller implementer en lokal RuleReasonRef som kan gi ut en enum eller record, unngå kode-magi
 */
@Deprecated(forRemoval = true)
public record RuleReasonRefImpl(String reasonCode, String reason) implements RuleReasonRef {

    @Override
    public String getReasonTextTemplate() {
        return reason;
    }

    @Override
    public String getReasonCode() {
        return reasonCode;
    }

}
