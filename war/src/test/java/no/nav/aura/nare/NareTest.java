package no.nav.aura.nare;


import no.nav.aura.nare.evaluation.Evaluation;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class NareTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true );
    Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo",false);


    @Test
    public void test() throws IOException {
        mor.setUttaksplan(Uttaksplan.INNEN_3_AAR);
        Evaluation evaluer = Ruleset.modrekvote().evaluer(Soknad.adopsjonsSøknada(mor).medSøker(far));
        evaluer.result();
        System.out.println(evaluer.result() + evaluer.reason());

        System.out.println(evaluer);
        Files.createDirectories(Paths.get("..","output"));
        Files.write(Paths.get("..","output","test.json"), evaluer.toString().getBytes());

        evaluer.ruleDescription();

    }


    @Test
    public void regelsett(){
        System.out.println(new Modrekvote().regelbeskrivelser());
    }



}