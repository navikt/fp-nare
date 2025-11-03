package no.nav.fpsak.nare.specification.modrekvote;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Rolle;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ModrekvoteTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
    Person barn = new Person("Theo", Rolle.BARN, "Barn", 0, 0, "Oslo", false);

    @Test
    void skal_evaluere_modrekvote() {
        mor.setUttaksplan(Uttaksplan.SENERE);
        Soknad soknad = Soknad.fodselSøknad(mor).medSøker(far);

        RuleService<Soknad> modrekvote = new Modrekvote();
        Evaluation evaluation = modrekvote.evaluer(soknad);

        assertThat(evaluation.result()).isEqualTo(Resultat.NEI);

        String asJson = EvaluationSerializer.asJson(evaluation);

//        System.out.println(asJson);

        Assertions.assertThat(asJson)
                .contains("FK_VK 10.4")
                .contains("FK_VK 10.5")
                .contains("FK_VK 10.6")
                .contains("FK_VK.10.A")
                .contains("FK_VK.10.B");

        EvaluationSummary evaluationSummary = new EvaluationSummary(evaluation);
        var leafReasons = evaluationSummary.leafEvaluations(Resultat.NEI, Resultat.IKKE_VURDERT).stream()
                .map(Evaluation::getOutcome)
                .filter(l -> l instanceof ModrekvoteRuleReason)
                .map(l -> (ModrekvoteRuleReason)l)
                .map(ModrekvoteRuleReason::utfall)
                .toList();
        Assertions.assertThat(leafReasons).containsOnly(ModrekvoteUtfall.UTFALL_09, ModrekvoteUtfall.UTFALL_11);

    }
}
