package no.nav.aura.nare.specifications;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

import java.util.Optional;

public class HarRettTilForeldrePenger extends AbstractSpecification<Soknad> {


    private Rolle rolle;

    private HarRettTilForeldrePenger(Rolle rolle){
        this.rolle = rolle;
    }

    public static HarRettTilForeldrePenger harRettTilForeldrePenger(Rolle rolle){
        return new HarRettTilForeldrePenger(rolle);
    }


    @Override
    public Evaluation evaluate(Soknad soknad) {

        Optional<Person> søker = soknad.getSøker(rolle);
        if (!søker.isPresent()){
             return Evaluation.no("Ingen søker med rolle {0} ", rolle);
        }else if(!søker.get().harRettTilForeldrepenger()){
            return Evaluation.no("Søker med rolle {0} har ikke rett til foreldrepenger", rolle);
        }else{
            return Evaluation.yes("Søker med rolle {0} har rett til foreldrepenger", rolle);
        }
    }

    @Override
    public String getDescription() {
        return "FK_VK 10.1 - har søker med rolle " + rolle +" rett til foreldrepenger";
    }
}
