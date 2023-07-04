package no.nav.fpsak.nare.evaluation.summary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.specification.modrekvote.ModrekvoteConditional;
import no.nav.fpsak.nare.specification.modrekvote.input.Person;
import no.nav.fpsak.nare.specification.modrekvote.input.Rolle;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;

class EvaluationSerializerTest {

    Person far = new Person("Truls", Rolle.FAR, "Nerd", 500000, 800, "Klofta", true);
    Person mor = new Person("Guro", Rolle.MOR, "Prosjektleder", 600000, 24, "Oslo", true);
    Soknad soknad = Soknad.adopsjonsSøknada(mor).medSøker(far);

    RuleService<Soknad> modrekvote = new ModrekvoteConditional();

    @Test
    void skal_gi_samme_resultat_hver_gang() {
        Evaluation evaluation = modrekvote.evaluer(soknad);

        String serialized1 = EvaluationSerializer.asJson(evaluation);
        String serialized2 = EvaluationSerializer.asJson(evaluation);

        Assertions.assertThat(serialized2).isEqualTo(serialized1);
    }

    @Test
    void skal_inkludere_versjoner_hvis_oppgitt() {
        Evaluation evaluation = modrekvote.evaluer(soknad);

        String serialisert = EvaluationSerializer.asJson(evaluation,
                new EvaluationVersion("nare", "1.2.1"),
                new EvaluationVersion("regel", "3.1.0"),
                new EvaluationVersion("fagsystem", "1r3f2adf3f2a1101")
        );

        Assertions.assertThat(serialisert)
                .contains("\"nare\" : \"1.2.1\"")
                .contains("\"regel\" : \"3.1.0\"")
                .contains("\"fagsystem\" : \"1r3f2adf3f2a1101\"");

    }
}