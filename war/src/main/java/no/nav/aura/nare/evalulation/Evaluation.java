package no.nav.aura.nare.evalulation;


import no.nav.aura.nare.evaluering.Resultat;

public interface Evaluation {

    Resultat result();
    String reason();
    String ruleDescription();
    String ruleIdentification();

}
