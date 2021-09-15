package no.nav.fpsak.nare.evaluation;

/**
 * Represents a key for a valid outcome of an evaluation (leaf).
 */
public interface RuleReasonRef {

    String getReasonTextTemplate();

    /*
     * Implementer heller en lokal DomemeRuleReasonRef som kan gi ut en enum eller record, unngå kode-magi
     * Diskusjon: Generification av RuleReasonRef<T> og T getReason() - vil treffe Evaluation
     * Diskusjon: Fjerne RuleReasonRefImpl (legg til repo-test) for å trigge lokale impl
     */
    @Deprecated(forRemoval = true)
    String getReasonCode();

}
