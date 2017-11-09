package no.nav.aura.nare.regelsettyper;

import static no.nav.aura.nare.input.Rolle.FAR;
import static no.nav.aura.nare.input.Rolle.MOR;
import static no.nav.aura.nare.input.Soknadstype.ADOPSJON;
import static no.nav.aura.nare.input.Soknadstype.FODSEL;
import static no.nav.aura.nare.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.aura.nare.input.Uttaksplan.SAMMENHENGENDE;
import static no.nav.aura.nare.regler.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.aura.nare.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteAdopsjonl;
import static no.nav.aura.nare.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteFodsel;
import static no.nav.aura.nare.regler.SoknadGjelder.søknadGjelder;
import static no.nav.fpsak.nare.specification.NotSpecification.ikke;

import no.nav.aura.nare.input.Soknad;
import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.Specification;

public class Modrekvote extends Ruleset implements RuleService<Soknad> {

    private final Ruleset ruleset = new Ruleset();

    public Modrekvote() {
    }

    @Override
    public Evaluation evaluer(Soknad data) {
        return getSpecification().evaluate(data);
    }

    @Override
    public Specification<Soknad> getSpecification() {
        Ruleset rs = ruleset;

        Specification<Soknad> harBeggeForeldreRettTilForeldrepenger = regel("FK_VK_10.1", "Har begge foreldre rett til foreldrepenger?",
                harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));
        Specification<Soknad> gjelderSøknadFødsel = rs.regel("FK_VK 10.2", "Gjelder søknad fødsel?", søknadGjelder(FODSEL));
        Specification<Soknad> gjelderSøknadAdopsjon = rs.regel("FK_VK 10.3", "Gjelder søknad adopsjon?", søknadGjelder(ADOPSJON));
        Specification<Soknad> harUttaksplanEtterFodsel = regel("FK_VK_10.4", "Har mor uttaksplan sammenhengende eller tre år etter fødsel?",
                harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR)));
        Specification<Soknad> harUttaksplanEtterAdopsjon = regel("FK_VK_10.5",
                "Har mor uttaksplan sammenhengende eller tre år etter adopsjon?", harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR));

        Specification<Soknad> vilkårForFødsel = regel("FK_VK.10.A",
                harBeggeForeldreRettTilForeldrepenger.og(gjelderSøknadFødsel).og(harUttaksplanEtterFodsel));

        Specification<Soknad> vilkårForAdopsjon = regel("FK_VK.10.B", harBeggeForeldreRettTilForeldrepenger
                .og(ikke(gjelderSøknadFødsel).medBeskrivelse("søknad gjelder ikke fødsel"))
                .og(gjelderSøknadAdopsjon)
                .og(harUttaksplanEtterAdopsjon));

        return regel("FK_VK.10", "Er vilkår for mødrekvote oppfylt for enten fødsel eller adopsjon?",
                vilkårForFødsel.eller(vilkårForAdopsjon));
    }

    @Override
    public RuleService<Soknad> medArgument(Object argument) {
        return this;
    }
}
