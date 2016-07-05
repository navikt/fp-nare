package no.nav.aura.nare.regelsettyper;

import no.nav.aura.nare.Regelsett;

import static no.nav.aura.nare.input.Rolle.FAR;
import static no.nav.aura.nare.input.Rolle.MOR;
import static no.nav.aura.nare.input.Soknadstype.FODSEL;
import static no.nav.aura.nare.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.aura.nare.input.Uttaksplan.SAMMENHENGENDE;
import static no.nav.aura.nare.specifications.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.aura.nare.specifications.HarUttaksplanForModreKvote.harUttaksplanForModreKvote;
import static no.nav.aura.nare.specifications.SoknadGjelder.søknadGjelder;

public class Modrekvote extends Regelsett {


    public Modrekvote(){

        regel("FK_VK 10.1", "Begge foreldre har rett til foreldrepenger", harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));
        regel("FK_VK 10.2", "Er det fødsel?", søknadGjelder(FODSEL));
        regel("FK_VK 10.4/FK_VK 10.5", "Skal mor ta ut mødrekvoten sammenhengende eller innen tre år etter fødsel? ", harUttaksplanForModreKvote(SAMMENHENGENDE).eller(harUttaksplanForModreKvote(INNEN_3_AAR)));
    }



}
