package no.nav.fpsak.nare.evaluation;

/*
 * Implementer gjerne en lokal DomemeRuleOutcome som kan gi ut en enum eller record, unngå getReasonCode og string-kode-magi
 * - Bruk T for typet grunn til utfallet
 * - Bruk R for beregnet resultat
 */
public record RuleOutcome<T, R>(T reason, String reasonCode, String reasonTextTemplate, R calculated) implements RuleReasonRef {

    public RuleOutcome(R calculated) {
        this(null,  null, "", calculated);
    }

    public RuleOutcome(T reason, String reasonCode) {
        this(reason,  reasonCode, "", null);
    }

    public RuleOutcome(T reason, String reasonCode, String reasonTextTemplate) {
        this(reason,  reasonCode, reasonTextTemplate, null);
    }

    @Override
    public String getReasonTextTemplate() {
        return reasonTextTemplate;
    }

    @Override
    public String getReasonCode() {
        return reasonCode;
    }

}
