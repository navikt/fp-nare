package no.nav.aura.nare;

import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import org.junit.Test;


public class NareTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Kløfta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", false );
    Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo",false);


    @Test
    public void mor(){


        Evaluering evaluering = Regelsett.mødrekvote().vurder(Familie.hovedsoker(mor).medSoker(far));
        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());
    }


    @Test
    public void far(){



        Evaluering evaluering = Regelsett.hovedForsorger().vurder(Familie.hovedsoker(far).forBarn(barn).medSoker(mor));

        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());

    }


    @Test
    public void regelsett(){
        System.out.println(Regelsett.hovedForsorger().regelbeskrivelser());
    }



}