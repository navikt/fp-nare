package no.nav.fpsak.nare.specification.modrekvote;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.EvaluationSummary;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Rolle;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan;

public class ModrekvoteConditionalOrSpecificationTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
    Person barn = new Person("Theo", Rolle.BARN, "Barn", 0, 0, "Oslo", false);

    @Test
    public void skal_evaluere_mødrekvote_conditional() throws Exception {
        mor.setUttaksplan(Uttaksplan.SENERE);
        Soknad soknad = Soknad.adopsjonsSøknada(mor).medSøker(far);

        RuleService<Soknad> modrekvote = new ModrekvoteConditional();
        Evaluation evaluation = modrekvote.evaluer(soknad);
        
        String asJson = EvaluationSerializer.asJson(evaluation);
//        System.out.println(asJson);
        
        EvaluationSummary evaluationSummary = new EvaluationSummary(evaluation);
        Collection<String> leafReasons = evaluationSummary.leafReasons(Resultat.NEI, Resultat.MANUELL_BEHANDLING);
        Assertions.assertThat(leafReasons).containsOnly("UTFALL_09");

        // TODO: unngå teste på string innhold, hent ut fra evaluation
        Assertions.assertThat(asJson)
                .doesNotContain("FK_VK 10.4")
                .doesNotContain("FK_VK 10.5")
                .doesNotContain("FK_VK.10.A")
                .contains("FK_VK 10.6")
                .contains("FK_VK.10.B");


    }

}
