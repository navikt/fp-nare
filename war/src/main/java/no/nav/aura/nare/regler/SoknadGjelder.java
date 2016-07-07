package no.nav.aura.nare.regler;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Soknadstype;
import no.nav.aura.nare.specifications.AbstractSpecification;
import no.nav.aura.nare.evalulation.Evaluation;

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

    @Override
    public String identifikator() {
        return "FK_VK 10.2/FK_VK 10.3";
    }

    @Override
    public String beskrivelse() {
        return "gjelder søknad " + soknadstype;
    }

}
