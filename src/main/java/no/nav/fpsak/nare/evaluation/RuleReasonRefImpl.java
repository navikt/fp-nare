package no.nav.fpsak.nare.evaluation;

import no.nav.fpsak.nare.specification.LeafSpecification;

/**
 * Representerer en unik output fra en kjøring av Specifications. Produseres normalt av {@link LeafSpecification}.
 */
public class RuleReasonRefImpl implements RuleReasonRef<String> {

    private final String reasonCode;
    private final String reasonTextTemplate;

    public RuleReasonRefImpl(String reasonCode, String reasonTextTemplate) {
        this.reasonCode = reasonCode;
        this.reasonTextTemplate = reasonTextTemplate;
    }

    @Override
    public String getReasonTextTemplate() {
        return reasonTextTemplate;
    }

    @Override
    public String getReasonCode() {
        return reasonCode;
    }

    @Override
    public String getReason() {
        return reasonCode;
    }
}
