package no.nav.fpsak.nare.specification.modrekvote.regler;

import static no.nav.fpsak.nare.specification.modrekvote.input.Rolle.MOR;

import java.util.Optional;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteUtfall;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype;
import no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan;

public class HarUttaksplanForModreKvote extends LeafSpecification<Soknad> {

    public static HarUttaksplanForModreKvote harUttaksplanForModreKvoteAdopsjonl(Uttaksplan uttaksplan) {
        return new HarUttaksplanForModreKvote("FK_VK 10.6", Soknadstype.ADOPSJON, uttaksplan);
    }

    public static HarUttaksplanForModreKvote harUttaksplanForModreKvoteFodsel(Uttaksplan uttaksplan) {
        return new HarUttaksplanForModreKvote("FK_VK 10.5", Soknadstype.FODSEL, uttaksplan);
    }

    private final Soknadstype soknadstype;

    private Uttaksplan uttaksplanModreKvote;

    private HarUttaksplanForModreKvote(String id, Soknadstype soknadstype, Uttaksplan uttaksplan) {
        super(id);
        this.soknadstype = soknadstype;
        this.uttaksplanModreKvote = uttaksplan;
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        Optional<Person> soker = soknad.getSøker(MOR);
        if (!soker.isPresent()) {
            return nei(ModrekvoteUtfall.ROLLE_INGEN_SØKER_MED_ROLLE, MOR);
        }

        if (!soker.get().getUttaksplan().isPresent()) {
            return nei(ModrekvoteUtfall.UTTAKSPLAN_MANGLER, MOR);
        }

        return (uttaksplanModreKvote.equals(soker.get().getUttaksplan().get()))
                ? ja(ModrekvoteUtfall.UTTAKSPLAN_MODREKVOTE_TAS, uttaksplanModreKvote.description(), soknadstype)
                : nei(ModrekvoteUtfall.UTTAKSPLAN_MODREKVOTE_TAS_IKKE, uttaksplanModreKvote.description(), soknadstype);
    }
}
