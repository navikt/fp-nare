package no.nav.aura.nare;


import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.aura.nare.specifications.Specification;


public class Ruleset {

   // private Map<Regelbeskrivelse, Specification> specifications = new HashMap<>();

    protected Specification specification;

    public static Ruleset modrekvote() {return new Modrekvote();
    }

    //public void regel(String id, String regelbeskrivelse, Specification specification) {
    //    specifications.put(Regelbeskrivelse.id(id).beskrivelse(regelbeskrivelse), specification);
    //}

    public Evaluation evaluer(Soknad soknad){
        return specification.evaluate(soknad);
    }
    public String regelbeskrivelser() {
        return specification.toString();
    }

}
