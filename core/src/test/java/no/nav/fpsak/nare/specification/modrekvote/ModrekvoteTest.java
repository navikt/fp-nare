package no.nav.fpsak.nare.specification.modrekvote;

import static org.assertj.core.api.Assertions.assertThat;

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

public class ModrekvoteTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
    Person barn = new Person("Theo", Rolle.BARN, "Barn", 0, 0, "Oslo", false);

    @Test
    public void skal_evaluere_modrekvote() throws Exception {
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
        Collection<String> leafReasons = evaluationSummary.leafReasons(Resultat.NEI, Resultat.IKKE_VURDERT);
        Assertions.assertThat(leafReasons).containsOnly("UTFALL_09", "UTFALL_11");

    }
}
