package no.nav.fpsak.nare.specification.modrekvote;

public enum ModrekvoteUtfall {

    UTFALL_01,
    UTFALL_02,
    UTFALL_03,
    UTFALL_04,
    UTFALL_05,
    UTFALL_07,
    UTFALL_08,
    UTFALL_09,
    UTFALL_10,
    UTFALL_11;

    public static final ModrekvoteRuleReason ROLLE_HAR_IKKE_RETT = new ModrekvoteRuleReason(UTFALL_04, "UTFALL_04",
            "Søker med rolle {0} har ikke rett til foreldrepenger");

    public static final ModrekvoteRuleReason ROLLE_HAR_RETT = new ModrekvoteRuleReason(UTFALL_05, "UTFALL_05", "Søker med rolle {0} har rett til foreldrepenger");

    public static final ModrekvoteRuleReason ROLLE_INGEN_SØKER_MED_ROLLE = new ModrekvoteRuleReason(UTFALL_03, "UTFALL_03", "Ingen søker med rolle {0} ");

    public static final ModrekvoteRuleReason ARBEIDET_TILSTREKKELIG = new ModrekvoteRuleReason(UTFALL_01, "UTFALL_01",
            "Person har jobbet {0} måneder, som er tilstrekkelig");

    public static final ModrekvoteRuleReason ARBEIDET_UTILSTREKKELIG = new ModrekvoteRuleReason(UTFALL_02, "UTFALL_02",
            "Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid");

    public static final ModrekvoteRuleReason UTTAKSPLAN_MANGLER = new ModrekvoteRuleReason(UTFALL_07, "UTFALL_07", "Det foreligger ingen uttaksplan for {0}");

    public static final ModrekvoteRuleReason UTTAKSPLAN_MODREKVOTE_TAS = new ModrekvoteRuleReason(UTFALL_08, "UTFALL_08", "Mødrekvote tas {0}");

    public static final ModrekvoteRuleReason UTTAKSPLAN_MODREKVOTE_TAS_IKKE = new ModrekvoteRuleReason(UTFALL_09, "UTFALL_09", "Mødrekvote tas ikke {0} {1}");

    public static final ModrekvoteRuleReason SOEKNADSTYPE_GJELDER = new ModrekvoteRuleReason(UTFALL_10, "UTFALL_10", "Søknad gjelder {0}");

    public static final ModrekvoteRuleReason SOEKNADSTYPE_GJELDER_IKKE = new ModrekvoteRuleReason(UTFALL_11, "UTFALL_11", "Søknad gjelder ikke {0}");

}