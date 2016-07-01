package no.nav.aura.nare;

/**
 * Created by j116592 on 30.06.2016.
 */
public enum Resultat {

    INNVILGET(1, true),
    MANUELL_BEHANDLING(2,false),
    AVSLAG(3,false);

    private final int weight;
    private final boolean isSatisfied;

    Resultat(int weight, boolean isSatisfied) {
        this.weight = weight;
        this.isSatisfied = isSatisfied;
    }

    public Resultat and(Resultat resultat) {
        return (this.weight > resultat.weight) ? this : resultat;
    }

    public Resultat or(Resultat resultat) {
        return (this.weight < resultat.weight) ? this : resultat;
    }
}
