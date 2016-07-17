package no.nav.aura.nare.regelsettyper;

import no.nav.aura.nare.Ruleset;
import no.nav.aura.nare.specifications.Specification;

import static no.nav.aura.nare.input.Rolle.FAR;
import static no.nav.aura.nare.input.Rolle.MOR;
import static no.nav.aura.nare.input.Soknadstype.ADOPSJON;
import static no.nav.aura.nare.input.Soknadstype.FODSEL;
import static no.nav.aura.nare.input.Uttaksplan.INNEN_3_AAR;
import static no.nav.aura.nare.input.Uttaksplan.SAMMENHENGENDE;
import static no.nav.aura.nare.regler.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static no.nav.aura.nare.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteAdopsjonl;
import static no.nav.aura.nare.regler.HarUttaksplanForModreKvote.harUttaksplanForModreKvoteFodsel;
import static no.nav.aura.nare.regler.SoknadGjelder.søknadGjelder;
import static no.nav.aura.nare.specifications.NotSpecification.ikke;

public class Modrekvote extends Ruleset {


    public Modrekvote(){

      //  regel("FK_VK 10.1", "Begge foreldre har rett til foreldrepenger", harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));
       // regel("FK_VK 10.2", "Er det fødsel?", søknadGjelder(FODSEL));
       // regel("FK_VK 10.4/FK_VK 10.5", "Skal mor ta ut mødrekvoten sammenhengende eller innen tre år etter fødsel? ", harUttaksplanForModreKvote(SAMMENHENGENDE).eller(harUttaksplanForModreKvote(INNEN_3_AAR)));

        //regel("Mødrekvote", "Vilkårsprøving om mødrekvote", getModreKvote());
        specification = getModreKvote();

    }

    public Specification getModreKvote(){

        Specification en = regel("FK_VK_10.1", "Har begge foreldre rett til foreldrepenger?", harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR)));
        Specification to = regel("FK_VK 10.2", "Gjelder søknad fødsel?", søknadGjelder(FODSEL));
        Specification tre = regel("FK_VK 10.3", "Gjelder søknad adopsjon?", søknadGjelder(ADOPSJON));
        Specification fire = regel("FK_VK_10.4", "Har mor uttaksplan sammenhengende eller tre år etter fødsel?", harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR)));
        Specification fem = regel("FK_VK_10.5", "Har mor uttaksplan sammenhengende eller tre år etter adopsjon?",  harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR));

        Specification førsteLedd = regel("FK_VK.10.A", en.og(to).og(fire));
        Specification andreledd = regel ("FK_VK.10.B", en.og(ikke(to).medBeskrivelse("søknad gjelder ikke fødsel")).og(tre).og(fem));
        return regel("FK_VK.10","Er vilkår for mødrekvote oppfylt for enten fødsel eller adopsjon?", førsteLedd.eller(andreledd));

    }



}
