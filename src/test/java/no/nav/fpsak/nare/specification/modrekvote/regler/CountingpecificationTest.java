package no.nav.fpsak.nare.specification.modrekvote.regler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import no.nav.fpsak.nare.RuleService;
import no.nav.fpsak.nare.Ruleset;
import no.nav.fpsak.nare.evaluation.Evaluation;
import no.nav.fpsak.nare.evaluation.Resultat;
import no.nav.fpsak.nare.evaluation.summary.EvaluationSummary;
import no.nav.fpsak.nare.specification.ConditionalOrSpecification;
import no.nav.fpsak.nare.specification.ForeachSpecification;
import no.nav.fpsak.nare.specification.LeafSpecification;
import no.nav.fpsak.nare.specification.SequenceSpecification;
import no.nav.fpsak.nare.specification.Specification;
import no.nav.fpsak.nare.specification.modrekvote.input.Soknad;

public class CountingpecificationTest {

    @Test
    public void skal_evaluere_regel_med_hvis_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(1);

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);

        var evaluationSummary = new EvaluationSummary(evaluation);
        Assertions.assertThat((Integer) evaluationSummary.output()).isEqualTo(8);
    }

    @Test
    public void skal_evaluere_regel_med_ellers_branch()  {
        var singleSpecification = new SingleRule();
        var evaluation = singleSpecification.evaluer(2);

        assertThat(evaluation.result()).isEqualTo(Resultat.JA);

        var evaluationSummary = new EvaluationSummary(evaluation);
        Assertions.assertThat((Integer) evaluationSummary.output()).isEqualTo(6);
    }

    private static class SingleRule implements RuleService<Integer> {

        public SingleRule() {
        }

        @Override
        public Evaluation evaluer(Integer data) {
            return getSpecification().evaluate(data);
        }

        @SuppressWarnings("unchecked")
        @Override
        public Specification<Integer> getSpecification() {
            Ruleset<Soknad> rs = new Ruleset<>();

            return new SequenceSpecification<>("Seq", "Seq",
                    new ForeachSpecification<>("Sum", "Sum", new AddOneLeaf(), List.of("A", "B", "C"), "bokstav"),
                    ConditionalOrSpecification.<Integer>regel("Hvis", "Så")
                            .hvis(new EvenLeaf(), new MultiplyByTwoLeaf())
                            .ellers(new AddOneLeaf()));
        }

    }

    private static class AddOneLeaf extends LeafSpecification<Integer> {
        private AddOneLeaf() {
            super("CarryOn");
        }

        @Override
        public Evaluation evaluate(Integer input) {
            var eval = ja();
            eval.setOutput(input + 1);
            return eval;
        }
    }

    private static class MultiplyByTwoLeaf extends LeafSpecification<Integer> {
        private MultiplyByTwoLeaf() {
            super("CarryOn");
        }

        @Override
        public Evaluation evaluate(Integer input) {
            var eval = ja();
            eval.setOutput(input * 2);
            return eval;
        }
    }

    private static class EvenLeaf extends LeafSpecification<Integer> {
        private EvenLeaf() {
            super("CarryOn");
        }

        @Override
        public Evaluation evaluate(Integer input) {
            return input % 2 == 0 ? ja() : nei();
        }
    }
}
