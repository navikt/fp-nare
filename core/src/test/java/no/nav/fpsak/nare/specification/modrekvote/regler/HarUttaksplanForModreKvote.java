package no.nav.fpsak.nare.specification.modrekvote.regler;


import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.AbstractSpecification;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype;
import no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan;

import static no.nav.fpsak.nare.specification.modrekvote.input.Rolle.MOR;

import java.util.Optional;

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
    public String identifikator() {
        return id;
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

}
