package no.nav.fpsak.nare.evaluation;

import no.nav.fpsak.nare.specification.LeafSpecification;

/**
 * Representerer en unik output fra en kjøring av Specifications. Produseres normalt av {@link LeafSpecification}.
 */
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
