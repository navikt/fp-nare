package no.nav.fpsak.nare.specification.modrekvote.input;

/**
 * Created by j116592 on 05.07.2016.
 */
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
