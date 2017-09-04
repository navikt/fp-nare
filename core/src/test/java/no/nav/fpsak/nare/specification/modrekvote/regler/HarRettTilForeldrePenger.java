package no.nav.fpsak.nare.specification.modrekvote.regler;

import java.util.Optional;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteUtfall;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Rolle;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;

public class HarRettTilForeldrePenger extends LeafSpecification<Soknad> {

    public static HarRettTilForeldrePenger harRettTilForeldrePenger(Rolle rolle) {
        return new HarRettTilForeldrePenger(rolle);
    }

    private Rolle rolle;

    private HarRettTilForeldrePenger(Rolle rolle) {
        this.rolle = rolle;
    }

    @Override
    public String beskrivelse() {
        return "Har søker med rolle " + rolle + " rett til foreldrepenger?";
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {

        Optional<Person> søker = soknad.getSøker(rolle);
        if (!søker.isPresent()) {
            return nei(ModrekvoteUtfall.UTFALL_03, "Ingen søker med rolle {0} ", rolle);
        } else if (!søker.get().harRettTilForeldrepenger()) {
            return nei(ModrekvoteUtfall.UTFALL_04, "Søker med rolle {0} har ikke rett til foreldrepenger", rolle);
        } else {
            return ja(ModrekvoteUtfall.UTFALL_05, "Søker med rolle {0} har rett til foreldrepenger", rolle);
        }
    }

    @Override
    public String identifikator() {
        return "FK_VK_10.1";
    }

}
