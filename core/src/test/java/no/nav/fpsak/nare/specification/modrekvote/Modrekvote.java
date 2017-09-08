package no.nav.fpsak.nare.specification.modrekvote;

import static no.nav.fpsak.nare.specification.NotSpecification.ikke;
import static no.nav.fpsak.nare.specification.modrekvote.input.Rolle.FAR;
import static no.nav.fpsak.nare.specification.modrekvote.input.Rolle.MOR;
import static no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype.ADOPSJON;
import static no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype.FODSEL;
import static no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan.SAMMENHENGENDE;
import static no.nav.fpsak.nare.specification.modrekvote.regler.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.fpsak.nare.specification.modrekvote.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteAdopsjonl;
import static no.nav.fpsak.nare.specification.modrekvote.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteFodsel;
import static no.nav.fpsak.nare.specification.modrekvote.regler.SoknadGjelder.søknadGjelder;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.Specification;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;

public class Modrekvote implements RuleService<Soknad> {

    public Modrekvote() {
    }

    @Override
    public Evaluation evaluer(Soknad data) {
        return getSpecification().evaluate(data);
    }

    @Override
    public Specification<Soknad> getSpecification() {
        Ruleset rs = new Ruleset();

        Specification<Soknad> harBeggeForeldreRettTilForeldrepenger = rs.regel("FK_VK_10.1", "Har begge foreldre rett til foreldrepenger?",
                harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));
        Specification<Soknad> gjelderSøknadFødsel = rs.regel("FK_VK 10.2", "Gjelder søknad fødsel?", søknadGjelder(FODSEL));
        Specification<Soknad> gjelderSøknadAdopsjon = rs.regel("FK_VK 10.3", "Gjelder søknad adopsjon?", søknadGjelder(ADOPSJON));
        Specification<Soknad> harUttaksplanEtterFodsel = rs.regel("FK_VK 10.4",
                "Har mor uttaksplan sammenhengende eller tre år etter fødsel?",
                harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR)));
        Specification<Soknad> harUttaksplanEtterAdopsjon = harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR);

        Specification<Soknad> vilkårForFødsel = rs.regel("FK_VK.10.A",
                harBeggeForeldreRettTilForeldrepenger.og(gjelderSøknadFødsel).og(harUttaksplanEtterFodsel));

        Specification<Soknad> vilkårForAdopsjon = rs.regel("FK_VK.10.B", harBeggeForeldreRettTilForeldrepenger
                .og(ikke(gjelderSøknadFødsel).medBeskrivelse("søknad gjelder ikke fødsel"))
                .og(gjelderSøknadAdopsjon)
                .og(harUttaksplanEtterAdopsjon));

        return rs.regel("FK_VK.10", "Er vilkår for mødrekvote oppfylt for enten fødsel eller adopsjon?",
                vilkårForFødsel.eller(vilkårForAdopsjon));
    }

}
