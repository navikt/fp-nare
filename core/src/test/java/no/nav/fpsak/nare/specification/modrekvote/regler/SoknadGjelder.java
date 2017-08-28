package no.nav.fpsak.nare.specification.modrekvote.regler;

import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.AbstractSpecification;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknadstype;

public class SoknadGjelder extends AbstractSpecification<Soknad> {


    private final Soknadstype soknadstype;

    private SoknadGjelder(Soknadstype soknadstype){
        this.soknadstype = soknadstype;
    }

    public static SoknadGjelder søknadGjelder(Soknadstype soknadstype){
        return new SoknadGjelder(soknadstype);
    }


    @Override
    public Evaluation evaluate(Soknad soknad) {
        if (soknad.getSoknadstype().equals(soknadstype)){
            return ja("Søknad gjelder {0}", soknadstype);
        }else{
            return nei("Søknad gjelder ikke {0}", soknadstype);
        }
    }

}
