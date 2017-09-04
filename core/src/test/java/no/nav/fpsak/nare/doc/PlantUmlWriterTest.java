package no.nav.fpsak.nare.doc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.fpsak.nare.RuleDescription;
import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.specification.modrekvote.Modrekvote;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteConditional;

public class PlantUmlWriterTest {
    @Test
    public void skriv_modrekvote_test_specification_til_diag() throws Exception {
        RuleService modrekvote = new ModrekvoteConditional();
        
        RuleDescription ruleDescription = modrekvote.getSpecification().ruleDescription();
        String output = ruleDescription.toPlantUmlActivityDiagram();
        System.out.println(output);
    }
}
