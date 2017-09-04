package no.nav.fpsak.nare.specification.modrekvote.regler;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteUtfall;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype;

public class SoknadGjelder extends LeafSpecification<Soknad> {

    public static SoknadGjelder søknadGjelder(Soknadstype soknadstype) {
        return new SoknadGjelder(soknadstype);
    }

    private final Soknadstype soknadstype;

    private SoknadGjelder(Soknadstype soknadstype) {
        super("");
        this.soknadstype = soknadstype;
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        if (soknad.getSoknadstype().equals(soknadstype)) {
            return ja(ModrekvoteUtfall.UTFALL_10, "Søknad gjelder {0}", soknadstype);
        } else {
            return nei(ModrekvoteUtfall.UTFALL_11, "Søknad gjelder ikke {0}", soknadstype);
        }
    }

}
