package no.nav.fpsak.nare.evaluation;

/**
 * Represents a key for a valid outcome of an evaluation (leaf).
 */
public interface RuleReasonRef<T> {

    String getReasonTextTemplate();

    @Deprecated(forRemoval = true) // Bruk heller enums/records enn String-kode
    String getReasonCode();

    T getReason();

}
