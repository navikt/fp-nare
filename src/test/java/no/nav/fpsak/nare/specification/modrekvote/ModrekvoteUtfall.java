package no.nav.fpsak.nare.specification.modrekvote;

import no.nav.fpsak.nare.evaluation.RuleReasonRefImpl;

public class ModrekvoteUtfall {

    public static final RuleReasonRefImpl ROLLE_HAR_IKKE_RETT = new RuleReasonRefImpl("UTFALL_04",
            "Søker med rolle {0} har ikke rett til foreldrepenger");

    public static final RuleReasonRefImpl ROLLE_HAR_RETT = new RuleReasonRefImpl("UTFALL_05", "Søker med rolle {0} har rett til foreldrepenger");

    public static final RuleReasonRefImpl ROLLE_INGEN_SØKER_MED_ROLLE = new RuleReasonRefImpl("UTFALL_03", "Ingen søker med rolle {0} ");

    public static final RuleReasonRefImpl ARBEIDET_TILSTREKKELIG = new RuleReasonRefImpl("UTFALL_01",
            "Person har jobbet {0} måneder, som er tilstrekkelig");

    public static final RuleReasonRefImpl ARBEIDET_UTILSTREKKELIG = new RuleReasonRefImpl("UTFALL_02",
            "Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid");

    public static final RuleReasonRefImpl UTTAKSPLAN_MANGLER = new RuleReasonRefImpl("UTFALL_07", "Det foreligger ingen uttaksplan for {0}");

    public static final RuleReasonRefImpl UTTAKSPLAN_MODREKVOTE_TAS = new RuleReasonRefImpl("UTFALL_08", "Mødrekvote tas {0}");

    public static final RuleReasonRefImpl UTTAKSPLAN_MODREKVOTE_TAS_IKKE = new RuleReasonRefImpl("UTFALL_09", "Mødrekvote tas ikke {0} {1}");

    public static final RuleReasonRefImpl SOEKNADSTYPE_GJELDER = new RuleReasonRefImpl("UTFALL_10", "Søknad gjelder {0}");

    public static final RuleReasonRefImpl SOEKNADSTYPE_GJELDER_IKKE = new RuleReasonRefImpl("UTFALL_11", "Søknad gjelder ikke {0}");

}