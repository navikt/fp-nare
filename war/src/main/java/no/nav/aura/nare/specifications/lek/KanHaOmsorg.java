package no.nav.aura.nare.specifications.lek;


import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class KanHaOmsorg extends AbstractSpecification<Soknad> {

    private KanHaOmsorg(){}

    public static KanHaOmsorg kanHaOmsorg(){
        return  new KanHaOmsorg();
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        boolean harInstOpphold = !soknad.getHovedsøker().getInstitusjonsOpphold().isEmpty();
        boolean registertInntekt = soknad.getHovedsøker().getInntekt() > 0;

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
