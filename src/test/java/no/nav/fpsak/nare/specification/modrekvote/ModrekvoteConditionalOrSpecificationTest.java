package no.nav.fpsak.nare.specification.modrekvote;

import java.util.Collection;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
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
        EvaluationSummary evaluationSummary = new EvaluationSummary(evaluation);
        Collection<ModrekvoteUtfall> leafReasons = evaluationSummary.leafEvaluations(Resultat.NEI, Resultat.IKKE_VURDERT).stream()
                .map(Evaluation::getOutcome)
                .map(o -> o instanceof ModrekvoteRuleReason m ? m.utfall() : null)
                .collect(Collectors.toList());
        Assertions.assertThat(leafReasons).containsOnly(ModrekvoteUtfall.UTFALL_09);

        Assertions.assertThat(asJson)
                .doesNotContain("FK_VK 10.4")
                .doesNotContain("FK_VK 10.5")
                .doesNotContain("FK_VK.10.A")
                .contains("FK_VK 10.6")
                .contains("FK_VK.10.B");

        System.out.println(asJson);

        System.out.println("\n\n\n\n\n-------------\n\n\n");
        System.out.println(EvaluationSerializer.asJson(modrekvote.getSpecification()));
    }

}
