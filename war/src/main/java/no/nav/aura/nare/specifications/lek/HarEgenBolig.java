package no.nav.aura.nare.specifications.lek;


import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class HarEgenBolig extends AbstractSpecification<Familie> {

    private HarEgenBolig(){}

    public static HarEgenBolig harEgenBolig(){
        return  new HarEgenBolig();
    }

    @Override
    public Evaluation evaluate(Familie familie) {
        String addresse = familie.getHovedsoker().getAddresse();
        switch (addresse){
            case "" :
                return Evaluation.no("Er ikke oppført med addresse");
            case "Kløfta":
                return Evaluation.manual("Meget skeptisk til folk fra Kløfta");
            default:
                return  Evaluation.yes("Er oppført med addresse {0}", addresse);
        }
    }

    @Override
    public String getDescription() {
        return "Søker har registert addresse";
    }
}
