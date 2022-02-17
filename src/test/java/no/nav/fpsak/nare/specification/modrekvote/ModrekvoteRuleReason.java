package no.nav.fpsak.nare.specification.modrekvote;

import no.nav.fpsak.nare.evaluation.RuleReasonRef;

/**
 * Tilpasset output
 */
public record ModrekvoteRuleReason(ModrekvoteUtfall utfall, String reasonCode, String reason) implements RuleReasonRef {

    @Override
    public String getReasonTextTemplate() {
        return reason;
    }

    @Override
    public String getReasonCode() {
        return reasonCode;
    }

}
