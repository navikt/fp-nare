package no.nav.aura.nare.specifications.lek;


import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class HarEgenBolig extends AbstractSpecification<Soknad> {

    private HarEgenBolig(){}

    public static HarEgenBolig harEgenBolig(){
        return  new HarEgenBolig();
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        String addresse = soknad.getHovedsøker().getAddresse();
        switch (addresse){
            case "" :
                return Evaluation.no("Er ikke oppfort med addresse");
            case "Klofta":
                return Evaluation.manual("Meget skeptisk til folk fra Klofta");
            default:
                return  Evaluation.yes("Er oppfort med addresse {0}", addresse);
        }
    }

    @Override
    public String getDescription() {
        return "Søker har registert addresse";
    }
}
