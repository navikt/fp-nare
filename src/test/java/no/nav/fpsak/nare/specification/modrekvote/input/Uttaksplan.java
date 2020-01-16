package no.nav.fpsak.nare.specification.modrekvote.input;

public enum Uttaksplan {

    SAMMENHENGENDE("sammenhengende etter"), INNEN_3_AAR("innen 3 år etter"), SENERE("senere enn 3 år etter");

    private String description;

    Uttaksplan(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }
}
