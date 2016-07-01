package no.nav.aura.nare;

import no.nav.aura.nare.input.Familie;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.specifications.common.Evaluation;

import java.util.List;
import java.util.Map;

/**
 * Created by j116592 on 28.06.2016.
 */
public class Nare {

    public static void main(String[] args) {

        //Mor er hovedforsørger og enten hovedsøker eller medsøker
        //Far er

        Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Kløfta");
        Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo" );
        Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo");

        Evaluering evaluering = Regelsett
                .hovedForsorger()
                .vurder(Familie.hovedsoker(mor).forBarn(barn).medSoker(far));

        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());

        System.out.println(Regelsett.hovedForsorger().regelbeskrivelser());

    }
}
