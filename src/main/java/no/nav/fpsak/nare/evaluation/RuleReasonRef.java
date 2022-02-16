package no.nav.fpsak.nare.evaluation;

/**
 * Represents a key for a valid outcome of an evaluation (leaf).
 * Senere: Generification av RuleReasonRef<T> og T getReason() - vil treffe Evaluation
 */
public interface RuleReasonRef {

    String getReasonTextTemplate();

    /*
     * Implementer heller en lokal DomemeRuleReasonRef som kan gi ut en enum eller record, unngå kode-magi
     */
    @Deprecated(forRemoval = true)
    String getReasonCode();

}
