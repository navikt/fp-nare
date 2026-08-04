package no.nav.fpsak.nare.evaluation.summary.doc.vis;

import no.nav.fpsak.nare.doc.RuleDescriptionDeserializedDigraph;
import no.nav.fpsak.nare.doc.vis.MermaidGenerator;
import no.nav.fpsak.nare.json.JsonOutput;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class MermaidGeneratorTest {

    private static String readJson(String resourceName) {
        try (var is = MermaidGeneratorTest.class.getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IllegalArgumentException("Fant ikke ressurs: " + resourceName);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Kunne ikke lese ressurs: " + resourceName, e);
        }
    }

    @Test
    void uttak_evaluation() {
        var json = readJson("uttak-evaluation.json");
        var mermaid = MermaidGenerator.evaluationAsMermaid(JsonOutput.fromJson(json, RuleDescriptionDeserializedDigraph.class));
        var expectedStart = """
                graph TD
                n1{ID:FP_VK 18
                Er det utsettelse?
                Resultat: NEI}
                """;
        assertThat(mermaid).isNotNull().startsWith(expectedStart);
        //System.out.println(mermaid);
    }

    @Test
    void beregning_sn_specification() {
        var json = readJson("beregning-sn-specification.json");
        var mermaid = MermaidGenerator.specificationAsMermaid(JsonOutput.fromJson(json, RuleDescriptionDeserializedDigraph.class));
        var expectedStart = """
                graph TD
                n1[[ID:FP_BR 2
                Foreslå beregningsgrunnlag for selvstendig næringsdrivende]]
                n2[ID:FP_BR 2.1 BP
                """;
        assertThat(mermaid).isNotNull().startsWith(expectedStart);
        //System.out.println(mermaid);
    }

    @Test
    void beregning_sn_evaluation() {
        var json = readJson("beregning-sn-evaluation.json");
        var mermaid = MermaidGenerator.evaluationAsMermaid(JsonOutput.fromJson(json, RuleDescriptionDeserializedDigraph.class));
        var expectedStart = """
                graph TD
                n1[[ID:FP_BR 2
                Foreslå beregningsgrunnlag for selvstendig næringsdrivende
                Resultat: JA]]
                n2[ID:FP_BR 2.1 BP
                """;
        assertThat(mermaid).isNotNull().startsWith(expectedStart);
        //System.out.println(mermaid);
    }

}
