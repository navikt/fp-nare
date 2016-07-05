package no.nav.aura.nare.specifications;

import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

import java.util.Optional;

public class HarRettTilForeldrePenger extends AbstractSpecification<Familie> {


    private Rolle rolle;

    private HarRettTilForeldrePenger(Rolle rolle){
        this.rolle = rolle;
    }

    public static HarRettTilForeldrePenger morHarRettTilForeldrePenger(){
        return new HarRettTilForeldrePenger(Rolle.MOR);
    }


    public static HarRettTilForeldrePenger farHarRettTilForeldrePenger(){
        return new HarRettTilForeldrePenger(Rolle.FAR);
    }


    @Override
    public Evaluation evaluate(Familie familie) {

        Optional<Person> soker = familie.getSoker(rolle);
        if (!soker.isPresent()){
             return Evaluation.no("Ingen søker med rolle {0} ", rolle);
        }else if(!soker.get().harRettTilForeldrepenger()){
            return Evaluation.no("Søker med rolle {0} har ikke rett til foreldrepenger", rolle);
        }else{
            return Evaluation.yes("Søker med rolle {0} har rett til foreldrepenger", rolle);
        }
    }

    @Override
    public String getDescription() {
        return "FK_VK 10.1 - har søker rett til foreldrepenger";
    }
}
