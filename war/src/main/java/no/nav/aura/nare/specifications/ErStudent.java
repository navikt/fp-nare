package no.nav.aura.nare.specifications;


import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class ErStudent extends AbstractSpecification<Familie> {

    private ErStudent(){}

    public static ErStudent erStudent(){
        return  new ErStudent();
    }

    @Override
    public Evaluation evaluate(Familie familie) {
        String yrke = familie.getHovedsoker().getYrke();
        return (yrke == "student") ? Evaluation.yes("Oppført som {0} (JA)", yrke) : Evaluation.no("Ikke oppgitt som student, men som {0})",yrke);
    }

    @Override
    public String getDescription() {
        return "Søker er oppført som student";
    }
}
