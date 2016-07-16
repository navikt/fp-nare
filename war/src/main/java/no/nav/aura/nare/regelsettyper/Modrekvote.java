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

        Specification en = harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR));
        Specification to = søknadGjelder(FODSEL);
        Specification tre = harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR));
        regel("test", "beskrivelse",  en.og(ikke(to)).og(tre));

    }

    public Specification getModreKvote(){

        Specification en = harRettTilForeldrePenger(MOR).og(harRettTilForeldrePenger(FAR));
        Specification to = søknadGjelder(FODSEL);
        Specification tre = harUttaksplanForModreKvoteFodsel(SAMMENHENGENDE).eller(harUttaksplanForModreKvoteFodsel(INNEN_3_AAR));
        Specification fire = søknadGjelder(ADOPSJON);
        Specification fem = harUttaksplanForModreKvoteAdopsjonl(INNEN_3_AAR);
        return
                (en
                .og(to)
                .og(tre)
                ).eller
                        (en
                        .og(ikke(to))
                        .og(fire)
                        .og(fem)
                );

    }



}
