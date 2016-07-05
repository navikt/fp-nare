package no.nav.aura.nare.specifications;


import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

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
            return Evaluation.no("Ingen søker med rolle {0}", MOR);
        }

        if (!soker.get().getUttaksplan().isPresent()){
            return Evaluation.no("Det foreligger ingen uttaksplan for {0}", MOR);
        }

        return (uttaksplanModreKvote.equals(soker.get().getUttaksplan().get()))
                ? Evaluation.yes("Mødrekvote tas {0}", uttaksplanModreKvote.description())
                : Evaluation.no("Mødrekvote tas ikke {0}", uttaksplanModreKvote.description());
//        switch (søker.get().getUttaksplan()){
//            case SAMMENHENGENDE:
//                return Evaluation.yes("Mødrekvote tas ut sammenhengende rett etter fødsel");
//            case SENERE:
//                return Evaluation.yes("Mødrekvote tas ut innen barnet fyller 3 år");
//            default:
//                return  Evaluation.no("Vilkår ikke oppfylt, og de resterende ukene av mødrekvoten faller bort");
//        }
    }

    @Override
    public String getDescription() {
        return "FK_VK 10.4/FK_VK 10.5 Uttaksplan for mødrekvote";
    }
}
