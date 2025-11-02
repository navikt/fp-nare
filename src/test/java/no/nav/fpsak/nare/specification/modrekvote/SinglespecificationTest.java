package no.nav.fpsak.nare.specification.modrekvote;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.Specification;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Rolle;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;
import no.nav.fpsak.nare.specification.modrekvote.input.Uttaksplan;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static no.nav.fpsak.nare.specification.modrekvote.input.Rolle.MOR;
import static no.nav.fpsak.nare.specification.modrekvote.regler.HarRettTilForeldrePenger.harRettTilForeldrePenger;
import static org.assertj.core.api.Assertions.assertThat;

class SinglespecificationTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);

    @Test
    void skal_evaluere_regel_med_kun_ett_leaf_ved_roten() {
        mor.setUttaksplan(Uttaksplan.SENERE);
        Soknad soknad = Soknad.fodselSøknad(mor).medSøker(far);

        RuleService<Soknad> singleSpecification = new SingleSpecification();
        Evaluation evaluation = singleSpecification.evaluer(soknad);

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);

        String asJson = EvaluationSerializer.asJson(evaluation);

        Assertions.assertThat(asJson).contains("FK_VK_10.1");

        EvaluationSummary evaluationSummary = new EvaluationSummary(evaluation);
        var leafReasons = evaluationSummary.leafEvaluations().stream()
                .map(Evaluation::getOutcome)
                .toList();
        Assertions.assertThat(leafReasons).hasSize(1);
        Assertions.assertThat(evaluationSummary.singleLeafEvaluation(Resultat.JA)).isPresent();

    }

    private static class SingleSpecification implements RuleService<Soknad> {

        public SingleSpecification() {
        }

        @Override
        public Evaluation evaluer(Soknad data) {
            return getSpecification().evaluate(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Specification<Soknad> getSpecification() {
            Ruleset<Soknad> rs = new Ruleset<>();

            return rs.regel("FK_VK_10.1", "Har mor rett til foreldrepenger?",
                    harRettTilForeldrePenger(MOR));
        }

    }
}
