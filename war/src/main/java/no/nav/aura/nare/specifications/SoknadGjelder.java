package no.nav.aura.nare.specifications;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Soknadstype;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

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
            return Evaluation.yes("Søknad gjelder {0}", soknadstype);
        }else{
            return Evaluation.no("Søknad gjelder ikke {0}", soknadstype);
        }
    }

    @Override
    public String getDescription() {
        return "FK_VK 10.2/FK_VK 10.3 - gjelder søknad " + soknadstype;
    }
}
