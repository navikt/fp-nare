package no.nav.aura.nare.regelsettyper;

import no.nav.aura.nare.Ruleset;

import static no.nav.aura.nare.specifications.lek.ErStudent.erStudent;
import static no.nav.aura.nare.specifications.HarArbeidetSisteMnd.harArbeidetSisteMnd;
import static no.nav.aura.nare.specifications.lek.HarEgenBolig.harEgenBolig;
import static no.nav.aura.nare.specifications.lek.KanHaOmsorg.kanHaOmsorg;
import static no.nav.aura.nare.specifications.common.NotSpecification.ikke;


public class Hovedforsorger extends Ruleset {

    public Hovedforsorger() {
        regel("1","Regel for oppfylling av arbeidsvilkår for primærforsorger", harArbeidetSisteMnd(12).eller(erStudent()));
        regel("2","Har jobbet litt i det minste!", harArbeidetSisteMnd(1));
        regel("3", "Er skikket for omsorg i egen bolig", harEgenBolig().og(kanHaOmsorg()));
        regel("4", "Er ikke student", ikke(erStudent()).og(ikke(kanHaOmsorg())));
    }
}