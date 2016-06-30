package no.nav.aura.nare.regelsettyper;

import no.nav.aura.nare.Regelsett;

import static no.nav.aura.nare.specifications.ErStudent.erStudent;
import static no.nav.aura.nare.specifications.HarArbeidetSisteMnd.harArbeidetSisteMnd;


public class Hovedforsorger extends Regelsett {

    public Hovedforsorger() {
        regel(1,"Regel for oppfylling av arbeidsvilkår for primærforsørger", harArbeidetSisteMnd(12).eller(erStudent()));
        regel(2,"Jobbe påå!", harArbeidetSisteMnd(12));
        regel(3, "HP er kul", harArbeidetSisteMnd(800));
    }


}