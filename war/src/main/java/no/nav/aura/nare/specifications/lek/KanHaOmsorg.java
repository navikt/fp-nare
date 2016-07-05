package no.nav.aura.nare.specifications.lek;


import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class KanHaOmsorg extends AbstractSpecification<Familie> {

    private KanHaOmsorg(){}

    public static KanHaOmsorg kanHaOmsorg(){
        return  new KanHaOmsorg();
    }

    @Override
    public Evaluation evaluate(Familie familie) {
        boolean harInstOpphold = !familie.getHovedsoker().getInstitusjonsOpphold().isEmpty();
        boolean registertInntekt = familie.getHovedsoker().getInntekt() > 0;

        if (!harInstOpphold && registertInntekt){
            return Evaluation.yes("Har ikke instopphold og registert inntekt.");
        }else if (harInstOpphold && registertInntekt){
            return  Evaluation.manual("Har instophold, men mye inntekt. Dett må sjekkes.");
        }else {
            return Evaluation.no("KAn ikke ha omsorg");
        }

    }

    @Override
    public String getDescription() {
        return "Sjekker om søker er skikket til å ha omsorg";
    }
}
