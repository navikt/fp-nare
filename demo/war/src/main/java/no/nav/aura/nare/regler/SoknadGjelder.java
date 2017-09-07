package no.nav.aura.nare.regler;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Soknadstype;
import no.nav.aura.nare.regelsettyper.ModrekvoteUtfall;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.LeafSpecification;

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
            return ja(ModrekvoteUtfall.SOEKNADSTYPE_GJELDER, soknadstype);
        } else {
            return nei(ModrekvoteUtfall.SOEKNADSTYPE_GJELDER_IKKE, soknadstype);
        }
    }

}
