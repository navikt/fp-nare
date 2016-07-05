package no.nav.aura.nare;

/**
 * Created by j116592 on 30.06.2016.
 */
public enum Resultat {

    INNVILGET(-1, "AVSLAG"),
    MANUELL_BEHANDLING(0, "MANUELL_BEHANDLING"),
    AVSLAG(1, "INNVILGET");

    private final int weight;
    private String inverse;


    Resultat(int weight, String inverse) {
        this.weight = weight;
        this.inverse = inverse;
    }



    public Resultat and(Resultat resultat) {
        return (this.weight > resultat.weight) ? this : resultat;
    }

    public Resultat or(Resultat resultat) {
        return (this.weight < resultat.weight) ? this : resultat;
    }

    public Resultat not(){
        return Resultat.valueOf(this.inverse);

    }
}
