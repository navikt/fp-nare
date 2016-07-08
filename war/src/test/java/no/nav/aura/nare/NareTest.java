package no.nav.aura.nare;


import no.nav.aura.nare.evalulation.Evaluation;
import no.nav.aura.nare.input.Person;
import no.nav.aura.nare.input.Rolle;
import no.nav.aura.nare.input.Soknad;
import no.nav.aura.nare.input.Uttaksplan;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;


import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class NareTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800,"Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true );
    Person barn = new Person("Theo", Rolle.BARN,"Barn", 0, 0, "Oslo",false);


    @Test
    public void test() throws IOException {
        mor.setUttaksplan(Uttaksplan.INNEN_3_AAR);
        Evaluation evaluer = Ruleset.modrekvote().evaluer(Soknad.fodselSøknad(mor).medSøker(far));
        evaluer.result();
        System.out.println(evaluer.result() + evaluer.reason());

        System.out.println(evaluer);
        Files.createDirectories(Paths.get("..","output"));
        Files.write(Paths.get("..","output","test.json"), evaluer.toString().getBytes());

        evaluer.ruleDescription();

    }
    /*@Test
    public void mor(){

        mor.setUttaksplan(Uttaksplan.INNEN_3_AAR);
        Evaluering evaluering = Ruleset.modrekvote().vurder(Soknad.adopsjonsSøknada(mor).medSøker(far));
        System.out.println(evaluering.resultat());
        System.out.println(evaluering.begrunnelse());
    }
*/


    @Test
    public void regelsett(){
        System.out.println(Ruleset.modrekvote().regelbeskrivelser());
    }



}