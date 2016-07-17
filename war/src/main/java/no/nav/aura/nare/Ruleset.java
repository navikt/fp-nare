package no.nav.aura.nare;


import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.aura.nare.specifications.Specification;


public class Ruleset {

    protected Specification specification;

    public static Ruleset modrekvote() {
        return new Modrekvote();
    }

    public Specification regel(String id, String beskrivelse, Specification specification) {
        return specification.medBeskrivelse(beskrivelse).medID(id);
    }

    public Specification regel(String id, Specification specification) {
        return specification.medID(id);
    }

    public Evaluation evaluer(Soknad soknad) {
        return specification.evaluate(soknad);
    }

    public String regelbeskrivelser() {
        return specification.ruleDescription().toString();
    }

}
