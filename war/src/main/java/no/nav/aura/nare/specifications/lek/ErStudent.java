package no.nav.aura.nare.specifications.lek;


import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.specifications.common.AbstractSpecification;
import no.nav.aura.nare.specifications.common.Evaluation;

public class ErStudent extends AbstractSpecification<Soknad> {

    private ErStudent(){}

    public static ErStudent erStudent(){
        return  new ErStudent();
    }

    @Override
    public Evaluation evaluate(Soknad soknad) {
        String yrke = soknad.getHovedsøker().getYrke();
        return (yrke == "student")
                ? Evaluation.yes("Oppfort som {0}", yrke)
                : Evaluation.no("Ikke oppgitt som student, men som {0})",yrke);
    }

    @Override
    public String getDescription() {
        return "Søker er oppfort som student";
    }
}
