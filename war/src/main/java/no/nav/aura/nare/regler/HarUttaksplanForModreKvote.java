package no.nav.aura.nare.regler;


import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Soknadstype;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.specifications.AbstractSpecification;
import no.nav.aura.nare.evalulation.Evaluation;

import java.util.Optional;

import static no.nav.aura.nare.input.Rolle.MOR;

public class HarUttaksplanForModreKvote extends AbstractSpecification<Soknad> {

    private final String id;
    private final Soknadstype soknadstype;
    private Uttaksplan uttaksplanModreKvote;

    private HarUttaksplanForModreKvote(String id, Soknadstype soknadstype, Uttaksplan uttaksplan){
        this.id = id;
        this.soknadstype = soknadstype;
        this.uttaksplanModreKvote = uttaksplan;
    }

    public static HarUttaksplanForModreKvote harUttaksplanForModreKvoteFodsel(Uttaksplan uttaksplan){
        return  new HarUttaksplanForModreKvote("FK_VK 10.4/FK_VK 10.5", Soknadstype.FODSEL, uttaksplan);
    }

    public static HarUttaksplanForModreKvote harUttaksplanForModreKvoteAdopsjonl(Uttaksplan uttaksplan){
        return  new HarUttaksplanForModreKvote("FK_VK 10.6", Soknadstype.ADOPSJON, uttaksplan);
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
                ? ja("Mødrekvote tas {0}", uttaksplanModreKvote.description(), soknadstype)
                : nei("Mødrekvote tas ikke {0} {1}", uttaksplanModreKvote.description(), soknadstype);
    }

    @Override
    public String identifikator() {
        return id; //"FK_VK 10.4/FK_VK 10.5";
    }

    @Override
    public String beskrivelse() {
        return "Uttaksplan for mødrekvote " + uttaksplanModreKvote.description();
    }
}
