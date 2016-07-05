package no.nav.aura.nare.specifications;


import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class HarArbeidetSisteMnd extends AbstractSpecification<Soknad> {


    private final int month;

    private HarArbeidetSisteMnd(int mnd){
        this.month = mnd;
    }

    public static HarArbeidetSisteMnd harArbeidetSisteMnd(int mnd){
        return new HarArbeidetSisteMnd(mnd);
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        int mndArbeid = soknad.getHovedsøker().getMndArbeid();
        return (mndArbeid > month)
                ? Evaluation.yes("Person har jobbet {0} måneder, som er tilstrekkelig", mndArbeid)
                : Evaluation.no("Person er oppfort med {0} mnd arbeid. Dekker ikke kravet til {1} mnd med arbeid", mndArbeid, month);
    }

    @Override
    public String getDescription() {
        return "Har arbeidet i minst " + month + " måneder";
    }
}
