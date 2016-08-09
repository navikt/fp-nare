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
    public void test() throws Exception {
        mor.setUttaksplan(Uttaksplan.INNEN_3_AAR);
        Evaluation evaluation = Ruleset.modrekvote().evaluer(Soknad.fodselSøknad(mor).medSøker(far));

        System.out.println(evaluation.result() + evaluation.reason());
        System.out.println(evaluation);

        Files.createDirectories(Paths.get("..","output"));
        Files.write(Paths.get("..","output","test.json"), evaluation.toString().getBytes());

        evaluation.ruleDescription();

    }


    @Test
    public void regelsett() throws Exception{

        Files.createDirectories(Paths.get("..","output"));
        Files.write(Paths.get("..","output","modrekvote.json"), new Modrekvote().regelbeskrivelser().toString().getBytes());
    }



}