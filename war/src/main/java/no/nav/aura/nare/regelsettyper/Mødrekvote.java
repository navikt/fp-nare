package no.nav.aura.nare.regelsettyper;

import no.nav.aura.nare.Regelsett;

import static no.nav.aura.nare.specifications.HarRettTilForeldrePenger.farHarRettTilForeldrePenger;
import static no.nav.aura.nare.specifications.HarRettTilForeldrePenger.morHarRettTilForeldrePenger;

public class Mødrekvote extends Regelsett {


    public Mødrekvote(){
        regel("FK_VK 10.1", "Begge foreldre har rett til foreldrepenger", morHarRettTilForeldrePenger().og(farHarRettTilForeldrePenger()));
    }



}
