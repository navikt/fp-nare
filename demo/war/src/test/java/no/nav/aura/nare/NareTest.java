package no.nav.aura.nare;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import no.nav.aura.nare.regelsettyper.Modrekvote;
import no.nav.fpsak.nare.evaluation.Evaluation;

public class NareTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
    Person barn = new Person("Theo", Rolle.BARN, "Barn", 0, 0, "Oslo", false);

    @Test
    public void test() throws Exception {
        mor.setUttaksplan(Uttaksplan.SENERE);
        Evaluation evaluation = new Modrekvote().evaluer(Soknad.fodselSøknad(mor).medSøker(far));

        System.out.println(evaluation.result() + evaluation.reason());
        System.out.println(evaluation);

        Files.createDirectories(Paths.get("..", "output"));
        Files.write(Paths.get("..", "output", "test.json"), evaluation.toString().getBytes());

        evaluation.ruleDescription();

    }

}