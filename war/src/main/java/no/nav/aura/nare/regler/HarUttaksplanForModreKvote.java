package no.nav.aura.nare.regler;


import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.specifications.AbstractSpecification;
import no.nav.aura.nare.evalulation.Evaluation;

import java.util.Optional;

import static no.nav.aura.nare.input.Rolle.MOR;

public class HarUttaksplanForModreKvote extends AbstractSpecification<Soknad> {

    private Uttaksplan uttaksplanModreKvote;

    private HarUttaksplanForModreKvote(Uttaksplan uttaksplan){
        this.uttaksplanModreKvote = uttaksplan;
    }

    public static HarUttaksplanForModreKvote harUttaksplanForModreKvote(Uttaksplan uttaksplan){
        return  new HarUttaksplanForModreKvote(uttaksplan);
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        Optional<Person> soker = soknad.getSøker(MOR);
        if (!soker.isPresent()){
            return nei("Ingen søker med rolle {0}", MOR);
        }

        if (!soker.get().getUttaksplan().isPresent()){
            return nei("Det foreligger ingen uttaksplan for {0}", MOR);
        }

        return (uttaksplanModreKvote.equals(soker.get().getUttaksplan().get()))
                ? ja("Mødrekvote tas {0}", uttaksplanModreKvote.description())
                : nei("Mødrekvote tas ikke {0}", uttaksplanModreKvote.description());
    }

    @Override
    public String identifikator() {
        return "FK_VK 10.4/FK_VK 10.5";
    }

    @Override
    public String beskrivelse() {
        return "Uttaksplan for mødrekvote " + uttaksplanModreKvote.description();
    }
}
