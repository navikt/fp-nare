package no.nav.fpsak.nare.specification.modrekvote.regler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.ServiceArgument;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleOutcome;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSerializer;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class ForeachDynamicTest {

    @Test
    public void skal_evaluere_regel_med_ellers_branch()  {
        var init = new Mellomregning(5);
        var singleSpecification = new SingleRule(init);
        var evaluation = singleSpecification.evaluer(init);

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);
        System.out.println(EvaluationSerializer.asJson(evaluation));
        System.out.println(EvaluationSerializer.asJson(singleSpecification.getSpecification()));
        var evaluationSummary = new EvaluationSummary(evaluation);
        var outcome = hentOutcome(evaluationSummary);
        assertThat(outcome.getAkkumulator()).hasSize(8); // Init = 5 elementer + 3 addletter
        assertThat(outcome.getSum()).isEqualTo(12); // Init = 5 + Foreach-7-iterasjoner
    }


    private Mellomregning hentOutcome(EvaluationSummary evaluationSummary) {
        return evaluationSummary.allOutcomes().stream()
            .map(o -> o instanceof RuleOutcome p ? p.calculated() : null)
            .map(o -> o instanceof Mellomregning ml ? ml : null)
            .findFirst().orElse(null);

    }

    private static class Mellomregning {
        private List<String> akkumulator;
        private int sum;
        Mellomregning(Integer tall) {
            this.sum = tall;
            this.akkumulator = new ArrayList<>();
            for (int i = 0; i < tall; i++) {
                akkumulator.add("a" + i);
            }
        }

        public List<String> getAkkumulator() {
            return akkumulator;
        }

        public int getSum() {
            return sum;
        }

        public void addToSum(int tall) {
            this.sum += tall;
        }

        public void addToAkkumulator(String element) {
            akkumulator.add(element + akkumulator.size());
        }
    }


    private static class SingleRule implements RuleService<Mellomregning> {

        private Mellomregning init;
        public SingleRule(Mellomregning init) {
            this.init = init;
        }

        @Override
        public Evaluation evaluer(Mellomregning data) {
            return getSpecification().evaluate(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Specification<Mellomregning> getSpecification() {
            var rs = new Ruleset<Mellomregning>();
            var regel = rs.sekvensRegel()
                .neste(new AddLetterLeaf())
                .neste(new AddLetterLeaf())
                .forAlle("bokstav", init.getAkkumulator(), new AddOneLeaf())
                .neste(new AddLetterLeaf())
                .siste(new SinkLeaf());
            return regel;//rs.regel("Seq", "Seq", regel);
        }

    }

    private static class AddOneLeaf extends LeafSpecification<Mellomregning> {
        private AddOneLeaf() {
            super("AddOne", "AddOne");
        }

        @Override
        public Evaluation evaluate(Mellomregning input) {
            input.addToSum(1);
            return ja();
        }
    }

    private static class AddLetterLeaf extends LeafSpecification<Mellomregning> {
        private AddLetterLeaf() {
            super("AddOne", "AddOne");
        }

        @Override
        public Evaluation evaluate(Mellomregning input) {
            input.addToAkkumulator("a");
            return ja();
        }

        @Override
        public Evaluation evaluate(Mellomregning input, ServiceArgument args) {
            Optional.ofNullable(args).filter(a -> "bokstav".equals(args.beskrivelse())).map(a -> (String)a.verdi()).ifPresent(input::addToAkkumulator);
            return ja();
        }
    }

    private static class SinkLeaf extends LeafSpecification<Mellomregning> {

        private SinkLeaf() {
            super("Collect", "Collect");
        }

        @Override
        public Evaluation evaluate(Mellomregning input) {
            var outcome = new RuleOutcome<>(input);
            return ja(outcome);
        }
    }
}
