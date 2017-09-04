package no.nav.fpsak.nare.doc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import no.nav.fpsak.nare.specification.modrekvote.Modrekvote;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteConditional;

public class DotWriterTest {
    @Test
    public void skriv_modrekvote_test_specification_til_diag() throws Exception {
        DotWriter dotWriter = new DotWriter();
        
        ModrekvoteConditional modrekvote = new ModrekvoteConditional();
        
        String output = dotWriter.writeToString(modrekvote);
//        System.out.println(output);
    }
}
