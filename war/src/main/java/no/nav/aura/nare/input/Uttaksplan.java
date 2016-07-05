package no.nav.aura.nare.input;

/**
 * Created by j116592 on 05.07.2016.
 */
public enum Uttaksplan {

    SAMMENHENGENDE("sammenhengende etter fødsel"), INNEN_3_AAR ("innen 3 år etter fødsel"), SENERE("senere enn 3 år etter fødsel");

    private String description;

    Uttaksplan(String description) {
        this.description = description;
    }

    public String description(){
        return description;
    }
}
