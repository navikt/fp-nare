package no.nav.aura.nare;

import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Uttaksplan;
import org.junit.Test;


public class NareTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true );
    Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo",false);


    @Test
    public void mor(){

        mor.setUttaksplan(Uttaksplan.INNEN_3_AAR);
        Evaluering evaluering = Regelsett.modrekvote().vurder(Soknad.adopsjonsSøknada(mor).medSøker(far));
        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());
    }


    @Test
    public void far(){



        Evaluering evaluering = Regelsett.hovedForsørger().vurder(Soknad.fodselSøknad(far).forBarn(barn).medSøker(mor));

        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());

    }


    @Test
    public void regelsett(){
        System.out.println(Regelsett.modrekvote().regelbeskrivelser());
    }



}