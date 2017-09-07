package no.nav.aura.nare.regelsettyper;

import no.nav.fpsak.nare.evaluation.DetailReasonKeyImpl;

public class ModrekvoteUtfall {

    public static final DetailReasonKeyImpl ROLLE_HAR_IKKE_RETT = new DetailReasonKeyImpl("UTFALL_04",
            "Søker med rolle {0} har ikke rett til foreldrepenger");

    public static final DetailReasonKeyImpl ROLLE_HAR_RETT = new DetailReasonKeyImpl("UTFALL_05", "Søker med rolle {0} har rett til foreldrepenger");

    public static final DetailReasonKeyImpl ROLLE_INGEN_SØKER_MED_ROLLE = new DetailReasonKeyImpl("UTFALL_03", "Ingen søker med rolle {0} ");

    public static final DetailReasonKeyImpl ARBEIDET_TILSTREKKELIG = new DetailReasonKeyImpl("UTFALL_01",
            "Person har jobbet {0} måneder, som er tilstrekkelig");

    public static final DetailReasonKeyImpl ARBEIDET_UTILSTREKKELIG = new DetailReasonKeyImpl("UTFALL_02",
            "Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid");

    public static final DetailReasonKeyImpl UTTAKSPLAN_MANGLER = new DetailReasonKeyImpl("UTFALL_07", "Det foreligger ingen uttaksplan for {0}");

    public static final DetailReasonKeyImpl UTTAKSPLAN_MODREKVOTE_TAS = new DetailReasonKeyImpl("UTFALL_08", "Mødrekvote tas {0}");

    public static final DetailReasonKeyImpl UTTAKSPLAN_MODREKVOTE_TAS_IKKE = new DetailReasonKeyImpl("UTFALL_09", "Mødrekvote tas ikke {0} {1}");

    public static final DetailReasonKeyImpl SOEKNADSTYPE_GJELDER = new DetailReasonKeyImpl("UTFALL_10", "Søknad gjelder {0}");

    public static final DetailReasonKeyImpl SOEKNADSTYPE_GJELDER_IKKE = new DetailReasonKeyImpl("UTFALL_11", "Søknad gjelder ikke {0}");

}