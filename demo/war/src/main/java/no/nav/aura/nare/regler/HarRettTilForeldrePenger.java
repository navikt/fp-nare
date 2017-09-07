package no.nav.aura.nare.regler;

import java.util.Optional;

import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.regelsettyper.ModrekvoteUtfall;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;

public class HarRettTilForeldrePenger extends LeafSpecification<Soknad> {

    public static HarRettTilForeldrePenger harRettTilForeldrePenger(Rolle rolle) {
        return new HarRettTilForeldrePenger(rolle);
    }

    private Rolle rolle;

    private HarRettTilForeldrePenger(Rolle rolle) {
        super("FK_VK_10.1");
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
            return nei(ModrekvoteUtfall.ROLLE_INGEN_SØKER_MED_ROLLE, rolle);
        } else if (!søker.get().harRettTilForeldrepenger()) {
            return nei(ModrekvoteUtfall.ROLLE_HAR_IKKE_RETT, rolle);
        } else {
            return ja(ModrekvoteUtfall.ROLLE_HAR_RETT, rolle);
        }
    }

}
