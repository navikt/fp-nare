package no.nav.fpsak.nare.evaluation;

public enum Resultat {

    JA(-1, "NEI"),
    MANUELL_BEHANDLING(0, "MANUELL_BEHANDLING"),
    NEI(1, "JA");

    private final int weight;
    private String inverse;

    Resultat(int weight, String inverse) {
        this.weight = weight;
        this.inverse = inverse;
    }

    public Resultat and(Resultat resultat) {
        return (this.weight > resultat.weight) ? this : resultat;
    }

    public Resultat not() {
        return Resultat.valueOf(this.inverse);

    }

    public Resultat or(Resultat resultat) {
        return (this.weight < resultat.weight) ? this : resultat;
    }
}
