package no.nav.fpsak.nare.specification.modrekvote.regler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.RuleOutcome;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.ForeachSpecification;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.SequenceSpecification;
import no.nav.fpsak.nare.specification.Specification;

public class CountingpecificationTest {

    @Test
    public void skal_evaluere_regel_med_hvis_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(new MellomregnInt(1));

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);

        var evaluationSummary = new EvaluationSummary(evaluation);
        var outcome = hentOutcome(evaluationSummary);
        assertThat(outcome).contains(8); // input = 1 + foreach (3 * 1) * cond/IfBranch (2) = 8
    }

    @Test
    public void skal_evaluere_regel_med_ellers_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(new MellomregnInt(2));

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);

        var evaluationSummary = new EvaluationSummary(evaluation);
        var outcome = hentOutcome(evaluationSummary);
        assertThat(outcome).contains(6); // input = 2 + foreach (3 * 1) + cond/OrBranch (1) = 6
    }

    private Optional<Integer> hentOutcome(EvaluationSummary evaluationSummary) {
        return evaluationSummary.singleLeafEvaluation(Resultat.JA)
                .map(Evaluation::getOutcome)
                .map(o -> o instanceof RuleOutcome p ? p.calculated() : null)
                .map(o -> o instanceof Integer i ? i : null);
    }

    private static class MellomregnInt {
        private Integer carryon;
        MellomregnInt(Integer integer) {
            this.carryon = integer;
        }
    }
    private static class SingleRule implements RuleService<MellomregnInt> {

        public SingleRule() {
        }

        @Override
        public Evaluation evaluer(MellomregnInt data) {
            return getSpecification().evaluate(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Specification<MellomregnInt> getSpecification() {
            return new SequenceSpecification<>("Seq", "Seq",
                    new ForeachSpecification<>("Sum", "Sum", new AddOneLeaf(), List.of("A", "B", "C"), "bokstav"),
                    ConditionalOrSpecification.<MellomregnInt>regel("Hvis", "Så")
                            .hvis(new EvenLeaf(), new MultiplyByTwoLeaf())
                            .ellers(new AddOneLeaf()));
        }

    }

    private static class AddOneLeaf extends LeafSpecification<MellomregnInt> {
        private AddOneLeaf() {
            super("AddOne");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            input.carryon = input.carryon + 1;
            var outcome = new RuleOutcome<>(input.carryon);
            return ja(outcome);
        }
    }

    private static class MultiplyByTwoLeaf extends LeafSpecification<MellomregnInt> {
        private MultiplyByTwoLeaf() {
            super("TimesTwo");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            input.carryon = input.carryon * 2;
            var outcome = new RuleOutcome<>(input.carryon);
            return ja(outcome);
        }
    }

    private static class EvenLeaf extends LeafSpecification<MellomregnInt> {
        private EvenLeaf() {
            super("CarryOn");
        }

        @Override
        public Evaluation evaluate(MellomregnInt input) {
            return input.carryon % 2 == 0 ? ja() : nei();
        }
    }
}
